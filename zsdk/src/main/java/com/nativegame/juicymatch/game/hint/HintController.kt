package com.nativegame.juicymatch.game.hint

import com.nativegame.juicymatch.algorithm.Match3Algorithm
import com.nativegame.juicymatch.algorithm.Match3Tile
import com.nativegame.juicymatch.asset.Preferences
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.asset.Textures
import com.nativegame.juicymatch.game.GameEvent
import com.nativegame.juicymatch.game.GameWorld
import com.nativegame.juicymatch.game.effect.TextEffect
import com.nativegame.juicymatch.game.hint.finder.HintFinderManager
import com.nativegame.juicymatch.game.layer.tile.Tile
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.juicymatch.level.Level
import com.nativegame.juicymatch.level.TutorialType
import com.nativegame.natyengine.engine.Engine
import com.nativegame.natyengine.entity.Entity
import com.nativegame.natyengine.entity.timer.Timer
import com.nativegame.natyengine.entity.timer.TimerEvent
import com.nativegame.natyengine.entity.timer.TimerEvent.TimerEventListener
import com.nativegame.natyengine.event.Event
import com.nativegame.natyengine.event.EventListener

/**
 * Created by Oscar Liang on 2022/02/23
 */
class HintController(engine: Engine?, tileSystem: TileSystem) : Entity(engine), EventListener,
    TimerEventListener {
    private val mTiles: Array<Array<Tile>>
    private val mTotalRow: Int
    private val mTotalCol: Int
    private val mHintFinder: HintFinderManager
    private val mHintModifier: HintModifier
    private val mShuffleText: TextEffect
    private val mHintTimer: Timer
    private val mShuffleTimer: Timer
    private val mSoundTimer: Timer
    private val mHintEnable: Boolean

    private var mHintTiles: MutableList<Tile?>? = null

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        mTiles = tileSystem.getChild()
        mTotalRow = tileSystem.getTotalRow()
        mTotalCol = tileSystem.getTotalColumn()
        mHintFinder = HintFinderManager()
        mHintModifier = HintModifier(engine)
        mShuffleText = TextEffect(engine, Textures.TEXT_SHUFFLE)
        mHintTimer = Timer(engine)
        mHintTimer.addTimerEvent(TimerEvent(this, HINT_TIMEOUT))
        mShuffleTimer = Timer(engine)
        mShuffleTimer.addTimerEvent(TimerEvent(this, SHUFFLE_TIMEOUT))
        mSoundTimer = Timer(engine)
        mSoundTimer.addTimerEvent(TimerEvent(this, SHUFFLE_SOUND_TIMEOUT))
        mSoundTimer.addTimerEvent(TimerEvent(this, SLIDE_SOUND_TIMEOUT))
        mHintEnable = Preferences.PREF_SETTING.getBoolean(Preferences.KEY_HINT, true)
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    public override fun onStart() {
        mSoundTimer.start()
    }

    override fun onEvent(event: Event?) {
        when (event as GameEvent?) {
            GameEvent.START_GAME ->                 // Start hint if no tutorial
                if (Level.LEVEL_DATA.tutorialType == TutorialType.NONE) {
                    startHint()
                }

            GameEvent.STOP_COMBO, GameEvent.REMOVE_BOOSTER, GameEvent.ADD_EXTRA_MOVES -> startHint()
            GameEvent.PLAYER_SWAP, GameEvent.ADD_BOOSTER -> stopHint()
            GameEvent.GAME_WIN, GameEvent.GAME_OVER -> removeFromGame()

            else -> {}
        }
    }

    override fun onTimerEvent(eventTime: Long) {
        if (eventTime == HINT_TIMEOUT) {
            showHintEffect()
        } else if (eventTime == SHUFFLE_TIMEOUT) {
            startHint()
        } else if (eventTime == SHUFFLE_SOUND_TIMEOUT) {
            Sounds.TILE_SHUFFLE.play()
        } else if (eventTime == SLIDE_SOUND_TIMEOUT) {
            Sounds.TILE_SLIDE.play()
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun startHint() {
        mHintTiles = mHintFinder.findHint(mTiles, mTotalRow, mTotalCol)
        if (mHintTiles == null) {
            // Shuffle the tile if hint not found
            shuffleTile()
            return
        }
        if (mHintEnable) {
            // Start timer and show effect if hint found
            mHintTimer.start()
        }
    }

    private fun stopHint() {
        mHintTimer.stop()
        removeHintEffect()
    }

    private fun showHintEffect() {
        mHintModifier.activate(mHintTiles)
    }

    private fun removeHintEffect() {
        if (mHintModifier.isRunning()) {
            mHintModifier.removeFromGame()
        }
    }

    private fun shuffleTile() {
        Match3Algorithm.shuffleTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
        mSoundTimer.start()
        mShuffleTimer.start()
        mShuffleText.activate(GameWorld.WORLD_WIDTH / 2f, GameWorld.WORLD_HEIGHT / 2f)
    } //========================================================

    companion object {
        private const val HINT_TIMEOUT: Long = 4000
        private const val SHUFFLE_TIMEOUT: Long = 2000
        private const val SHUFFLE_SOUND_TIMEOUT: Long = 1000
        private const val SLIDE_SOUND_TIMEOUT: Long = 300
    }
}
