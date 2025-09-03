package com.nativegame.juicymatch.game.swap

import com.nativegame.juicymatch.algorithm.Match3Algorithm
import com.nativegame.juicymatch.algorithm.Match3Tile
import com.nativegame.juicymatch.game.GameEvent
import com.nativegame.juicymatch.game.GameWorld
import com.nativegame.juicymatch.game.algorithm.special.combine.SpecialCombineHandlerManager
import com.nativegame.juicymatch.game.layer.tile.Tile
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.juicymatch.game.swap.SwapModifier.SwapListener
import com.nativegame.natyengine.engine.Engine
import com.nativegame.natyengine.entity.Entity
import com.nativegame.natyengine.entity.timer.Timer
import com.nativegame.natyengine.entity.timer.TimerEvent
import com.nativegame.natyengine.entity.timer.TimerEvent.TimerEventListener
import com.nativegame.natyengine.event.Event
import com.nativegame.natyengine.event.EventListener
import com.nativegame.natyengine.input.touch.TouchEvent
import com.nativegame.natyengine.input.touch.TouchEventListener

/**
 * Created by Oscar Liang on 2022/02/23
 */
class SwapController(engine: Engine?, tileSystem: TileSystem) : Entity(engine), TouchEventListener,
    EventListener, SwapListener, TimerEventListener {
    private val mTiles: Array<Array<Tile>>
    private val mTotalRow: Int
    private val mTotalCol: Int
    private val mMarginX: Int
    private val mMarginY: Int
    private val mSwapModifier: SwapModifier
    private val mSwapBackModifier: SwapModifier
    private val mSpecialCombineHandler: SpecialCombineHandlerManager
    private val mTimer: Timer

    private var mTouchDownTile: Tile? = null
    private var mTouchUpTile: Tile? = null
    private var mEnable = false

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        mTiles = tileSystem.getChild()
        mTotalRow = tileSystem.getTotalRow()
        mTotalCol = tileSystem.getTotalColumn()
        mMarginX = (GameWorld.WORLD_WIDTH - mTotalCol * 300) / 2
        mMarginY = (GameWorld.WORLD_HEIGHT - mTotalRow * 300) / 2
        mSwapModifier = SwapModifier(engine)
        mSwapModifier.setListener(this)
        mSwapBackModifier = SwapModifier(engine)
        mSpecialCombineHandler = SpecialCombineHandlerManager(engine)
        mTimer = Timer(engine)
        mTimer.addTimerEvent(TimerEvent(this))
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun onTouchEvent(type: Int, touchX: Float, touchY: Float) {
        if (!mEnable) {
            return
        }
        when (type) {
            TouchEvent.TOUCH_DOWN -> {
                // Check is out of bound
                if (touchX < mMarginX || touchY < mMarginY || touchX > GameWorld.WORLD_WIDTH - mMarginX || touchY > GameWorld.WORLD_HEIGHT - mMarginY) {
                    return
                }
                val touchDownCol = ((touchX - mMarginX) / 300).toInt()
                val touchDownRow = ((touchY - mMarginY) / 300).toInt()
                mTouchDownTile = mTiles[touchDownRow]!![touchDownCol]
                mTouchDownTile!!.selectTile()
            }

            TouchEvent.TOUCH_UP -> {
                if (mTouchDownTile == null) {
                    return
                }
                mTouchDownTile!!.unSelectTile()
                val row = mTouchDownTile!!.getRow()
                val col = mTouchDownTile!!.getColumn()
                if (touchX < mTouchDownTile!!.getX()) {
                    // Swap left tile
                    if (col > 0) {
                        mTouchUpTile = mTiles[row]!![col - 1]
                    }
                } else if (touchX > mTouchDownTile!!.getEndX()) {
                    // Swap right tile
                    if (col < mTotalCol - 1) {
                        mTouchUpTile = mTiles[row]!![col + 1]
                    }
                } else if (touchY < mTouchDownTile!!.getY()) {
                    // Swap up tile
                    if (row > 0) {
                        mTouchUpTile = mTiles[row - 1]!![col]
                    }
                } else if (touchY > mTouchDownTile!!.getEndY()) {
                    // Swap down tile
                    if (row < mTotalRow - 1) {
                        mTouchUpTile = mTiles[row + 1]!![col]
                    }
                }
                // Check is tile swappable
                if (mTouchUpTile != null && mTouchUpTile!!.isSwappable() && mTouchDownTile!!.isSwappable()) {
                    Match3Algorithm.swapTile(mTiles as Array<Array<Match3Tile>>, mTouchDownTile!!, mTouchUpTile!!)
                    mSwapModifier.activate(mTouchDownTile, mTouchUpTile)
                    mEnable = false
                }
                // Reset touch tile
                mTouchDownTile = null
                mTouchUpTile = null
            }
        }
    }

    override fun onSwap(tileA: Tile, tileB: Tile) {
        // Check is special combine detected
        if (checkSpecialCombine(tileA, tileB)) {
            mTimer.start()
        } else {
            // Otherwise, check is player match any tile
            Match3Algorithm.findMatchTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
            if (Match3Algorithm.isMatch(mTiles, mTotalRow, mTotalCol)) {
                // Start the Algorithm if found
                tileA.setSelect(true)
                tileB.setSelect(true)
                dispatchEvent(GameEvent.PLAYER_SWAP)
            } else {
                // Swap back if not found
                Match3Algorithm.swapTile(mTiles, tileA, tileB)
                mSwapBackModifier.activate(tileA, tileB)
                mEnable = true
            }
        }
    }

    override fun onEvent(event: Event?) {
        when (event as GameEvent?) {
            GameEvent.START_GAME, GameEvent.STOP_COMBO, GameEvent.REMOVE_BOOSTER, GameEvent.ADD_EXTRA_MOVES -> mEnable =
                true

            GameEvent.ADD_BOOSTER -> mEnable = false
            GameEvent.GAME_WIN, GameEvent.GAME_OVER -> removeFromGame()
            else->{}
        }

    }

    override fun onTimerEvent(eventTime: Long) {
        dispatchEvent(GameEvent.PLAYER_SWAP)
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun checkSpecialCombine(tileA: Tile?, tileB: Tile?): Boolean {
        val handler =
            mSpecialCombineHandler.checkSpecialCombine(mTiles, tileA, tileB, mTotalRow, mTotalCol)
        if (handler != null) {
            mTimer.getAllTimerEvents().get(0).setEventTime(handler.getStartDelay())
            return true
        }

        return false
    } //========================================================
}
