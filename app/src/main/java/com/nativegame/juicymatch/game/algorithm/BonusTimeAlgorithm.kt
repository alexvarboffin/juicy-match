package com.nativegame.juicymatch.game.algorithm

import android.graphics.Color
import com.nativegame.juicymatch.algorithm.Match3Algorithm
import com.nativegame.juicymatch.algorithm.Match3Tile
import com.nativegame.juicymatch.asset.Fonts
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.asset.Textures
import com.nativegame.juicymatch.game.GameEvent
import com.nativegame.juicymatch.game.GameLayer
import com.nativegame.juicymatch.game.GameWorld
import com.nativegame.juicymatch.game.effect.TextEffect
import com.nativegame.juicymatch.game.effect.flash.TransformFlashEffectSystem
import com.nativegame.juicymatch.game.layer.tile.FruitType
import com.nativegame.juicymatch.game.layer.tile.SpecialType
import com.nativegame.juicymatch.game.layer.tile.Tile
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.juicymatch.level.Level
import com.nativegame.natyengine.engine.Engine
import com.nativegame.natyengine.entity.text.Text
import com.nativegame.natyengine.input.touch.TouchEvent
import com.nativegame.natyengine.input.touch.TouchEventListener
import com.nativegame.natyengine.util.RandomUtils

/**
 * Created by Oscar Liang on 2022/02/23
 */
class BonusTimeAlgorithm(engine: Engine?, tileSystem: TileSystem) :
    BaseAlgorithm(engine, tileSystem), TouchEventListener {
    private val mTransformFlashEffect: TransformFlashEffectSystem
    private val mBonusText: TextEffect
    private val mSkipText: SkipText
    private val mBonusSpecialTypes: Array<SpecialType>

    private var mState: AlgorithmState? = null
    private var mRemainingMove = 0
    private var mTotalTime: Long = 0
    private var mPauseTime: Long = 300
    private var mBonusIntervalTime: Long = 200
    private var mIsAddBonus = false
    private var mIsSkipBonus = false

    private enum class AlgorithmState {
        CHECK_MATCH,
        MOVE_TILE,
        PAUSE_TILE,
        ADD_BONUS
    }

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        mTransformFlashEffect = TransformFlashEffectSystem(engine, MAX_TRANSFORM_NUM)
        mBonusText = TextEffect(engine, Textures.TEXT_BONUS)
        mSkipText = SkipText(engine, 1500, 600, "Tap to skip")
        mBonusSpecialTypes = arrayOf<SpecialType>(
            SpecialType.ROW_STRIPED,
            SpecialType.COLUMN_STRIPED,
            SpecialType.EXPLOSIVE
        )
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    public override fun onStart() {
        mSkipText.activate(GameWorld.WORLD_WIDTH / 2f, GameWorld.WORLD_HEIGHT / 2f + 1650)
        mBonusText.activate(GameWorld.WORLD_WIDTH / 2f, GameWorld.WORLD_HEIGHT / 2f)
    }

    public override fun onRemove() {
        if (mSkipText.isRunning()) {
            mSkipText.removeFromGame()
        }
        if (mBonusText.isRunning()) {
            mBonusText.removeFromGame()
        }
    }

    public override fun onUpdate(elapsedMillis: Long) {
        when (mState) {
            AlgorithmState.CHECK_MATCH -> checkMatch()
            AlgorithmState.MOVE_TILE -> moveTile(elapsedMillis)
            AlgorithmState.PAUSE_TILE -> {
                mTotalTime += elapsedMillis
                if (mTotalTime >= mPauseTime) {
                    mState = AlgorithmState.MOVE_TILE
                    mTotalTime = 0
                }
            }

            AlgorithmState.ADD_BONUS -> {
                mTotalTime += elapsedMillis
                if (mTotalTime >= mBonusIntervalTime) {
                    // Update remaining move
                    if (mRemainingMove == 0) {
                        mState = AlgorithmState.CHECK_MATCH
                    } else {
                        addBonus()
                        dispatchEvent(GameEvent.ADD_BONUS)
                        mRemainingMove--
                    }
                    mTotalTime = 0
                }
            }

            else -> {}
        }
    }

    override fun initAlgorithm() {
    }

    override fun startAlgorithm() {
        Level.LEVEL_DATA.fruitCount = 5
        mRemainingMove = Level.LEVEL_DATA.move
        mState = AlgorithmState.CHECK_MATCH
        addToGame()
        mTotalTime = 0
    }

    override fun removeAlgorithm() {
    }

    override fun onTouchEvent(type: Int, touchX: Float, touchY: Float) {
        if (mIsSkipBonus) {
            return
        }
        // Skip the bonus time if player touch screen
        if (type == TouchEvent.TOUCH_DOWN) {
            skipBonus()
            mIsSkipBonus = true
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun checkMatch() {
        Match3Algorithm.findMatchTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
        popSpecialTile()
        // Check is any matches found
        if (!Match3Algorithm.isMatch(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)) {
            // Check is bonus added yet
            if (!mIsAddBonus) {
                // Convert remaining moves to bonus if not
                mState = AlgorithmState.ADD_BONUS
                mIsAddBonus = true
            } else {
                // Otherwise, stop the bonus time and notify GameController
                dispatchEvent(GameEvent.BONUS_TIME_END)
                removeFromGame()
            }
        } else {
            // Run the algorithm if found
            mSpecialTileFinder.findSpecialTile(mTiles, mTotalRow, mTotalCol)
            Match3Algorithm.playTileEffect(mTiles, mTotalRow, mTotalCol)
            Match3Algorithm.checkUnreachableTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
            Match3Algorithm.resetMatchTile(mTiles, mTotalRow, mTotalCol)
            mState = AlgorithmState.PAUSE_TILE
        }
    }

    private fun moveTile(elapsedMillis: Long) {
        Match3Algorithm.moveTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol, elapsedMillis)
        // Update waiting tile state when moving
        if (Match3Algorithm.isWaiting(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)) {
            Match3Algorithm.findUnreachableTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
            Match3Algorithm.checkWaitingTile(mTiles, mTotalRow, mTotalCol)
            Match3Algorithm.resetMatchTile(mTiles, mTotalRow, mTotalCol)
            // Important to not check isMoving(), so the tile will move continuously
        }
        // Check match if tiles stop moving
        if (!Match3Algorithm.isMoving(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)) {
            Sounds.TILE_BOUNCE.play()
            mState = AlgorithmState.CHECK_MATCH
        }
    }

    private fun popSpecialTile() {
        for (i in 0..<mTotalRow) {
            for (j in 0..<mTotalCol) {
                val t = mTiles[i][j]
                // Pop one special tile at a time
                if (t.specialType != SpecialType.NONE) {
                    t.popTile()
                    return
                }
            }
        }
    }

    private fun addBonus() {
        var targetTile: Tile
        do {
            // Chose a random tile
            val row = RandomUtils.nextInt(mTotalRow)
            val col = RandomUtils.nextInt(mTotalCol)
            targetTile = mTiles[row][col]
        } while (targetTile.getTileType() === FruitType.NONE
            || targetTile.getSpecialType() != SpecialType.NONE
        )

        // Update special type and add transform effect
        targetTile.setSpecialType(mBonusSpecialTypes[RandomUtils.nextInt(mBonusSpecialTypes.size)])
        mTransformFlashEffect.activate(targetTile.getCenterX(), targetTile.getCenterY())
        Sounds.ADD_BONUS.play()
    }

    private fun skipBonus() {
        mPauseTime = 0
        mBonusIntervalTime = 50
        dispatchEvent(GameEvent.BONUS_TIME_SKIP)
        mSkipText.removeFromGame()
    }

    //========================================================
    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    private class SkipText(engine: Engine?, width: Int, height: Int, text: String?) :
        Text(engine, width, height, text) {
        //--------------------------------------------------------
        // Constructors
        //--------------------------------------------------------
        init {
            mPaint.setColor(Color.WHITE)
            setTextSize(300f)
            setTextTypeface(Fonts.BALOO)
            setLayer(GameLayer.EFFECT_LAYER)
        }

        //========================================================
        //--------------------------------------------------------
        // Methods
        //--------------------------------------------------------
        fun activate(x: Float, y: Float) {
            setCenterX(x)
            setCenterY(y)
            addToGame()
        } //========================================================
    } //========================================================

    companion object {
        private const val MAX_TRANSFORM_NUM = 20
    }
}
