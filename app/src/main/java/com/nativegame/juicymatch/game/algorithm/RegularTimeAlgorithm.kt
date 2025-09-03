package com.nativegame.juicymatch.game.algorithm

import com.nativegame.juicymatch.algorithm.Match3Algorithm
import com.nativegame.juicymatch.algorithm.Match3Tile
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.game.GameEvent
import com.nativegame.juicymatch.game.algorithm.layer.LayerHandlerManager
import com.nativegame.juicymatch.game.algorithm.target.TargetHandlerManager
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.juicymatch.level.Level
import com.nativegame.natyengine.engine.Engine

/**
 * Created by Oscar Liang on 2022/02/23
 */
class RegularTimeAlgorithm //--------------------------------------------------------
// Constructors
//--------------------------------------------------------
    (
    engine: Engine?, tileSystem: TileSystem, private val mLayerHandlerManager: LayerHandlerManager,
    private val mTargetHandlerManager: TargetHandlerManager
) : BaseAlgorithm(engine, tileSystem) {
    private var mState: AlgorithmState? = null
    private var mTotalTime: Long = 0

    private enum class AlgorithmState {
        CHECK_MATCH,
        MOVE_TILE,
        PAUSE_TILE
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    public override fun onUpdate(elapsedMillis: Long) {
        when (mState) {
            AlgorithmState.CHECK_MATCH -> checkMatch()
            AlgorithmState.MOVE_TILE -> moveTile(elapsedMillis)
            AlgorithmState.PAUSE_TILE -> {
                // We pause the tiles for a while before moving
                mTotalTime += elapsedMillis
                if (mTotalTime >= TIME_TO_PAUSE) {
                    mState = AlgorithmState.MOVE_TILE
                    mTotalTime = 0
                }
            }
            else -> {}
        }
    }

    override fun initAlgorithm() {
        mLayerHandlerManager.initLayers(mTiles, mTotalRow, mTotalCol)
        Match3Algorithm.initTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
    }

    override fun startAlgorithm() {
        mState = AlgorithmState.CHECK_MATCH
        addToGame()
        mTotalTime = 0
    }

    override fun removeAlgorithm() {
        mLayerHandlerManager.removeLayers(mTiles, mTotalRow, mTotalCol)
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun checkMatch() {
        Match3Algorithm.findMatchTile(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)
        mTargetHandlerManager.checkTargets(mTiles, mTotalRow, mTotalCol)
        mLayerHandlerManager.updateLayers(mTargetHandlerManager, mTiles, mTotalRow, mTotalCol)
        if (mTargetHandlerManager.isTargetChange()) {
            dispatchEvent(GameEvent.PLAYER_COLLECT)
        }
        // Check is any matches found
        if (!Match3Algorithm.isMatch(mTiles as Array<Array<Match3Tile>>, mTotalRow, mTotalCol)) {
            // Dispatch next event if no more matches
            if (mTargetHandlerManager.isTargetComplete()) {
                // Player collect all the target
                dispatchEvent(GameEvent.GAME_WIN)
            } else if (Level.LEVEL_DATA.move == 0) {
                // Player run out of moves
                dispatchEvent(GameEvent.PLAYER_OUT_OF_MOVE)
            } else {
                // Continue the game
                dispatchEvent(GameEvent.STOP_COMBO)
            }
            removeFromGame()
        } else {
            // Add one combo
            dispatchEvent(GameEvent.ADD_COMBO)
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
        if (Match3Algorithm.isWaiting(mTiles, mTotalRow, mTotalCol)) {
            Match3Algorithm.findUnreachableTile(mTiles, mTotalRow, mTotalCol)
            Match3Algorithm.checkWaitingTile(mTiles, mTotalRow, mTotalCol)
            Match3Algorithm.resetMatchTile(mTiles, mTotalRow, mTotalCol)
            // Important to not check isMoving(), so the tile will move continuously
        }
        // Check match if tiles stop moving
        if (!Match3Algorithm.isMoving(mTiles, mTotalRow, mTotalCol)) {
            Sounds.TILE_BOUNCE.play()
            mState = AlgorithmState.CHECK_MATCH
        }
    } //========================================================

    companion object {
        private const val TIME_TO_PAUSE = 300
    }
}
