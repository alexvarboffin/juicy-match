package com.nativegame.juicymatch.game.booster

import com.nativegame.juicymatch.algorithm.Match3Algorithm
import com.nativegame.juicymatch.algorithm.Match3Tile
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.asset.Textures
import com.nativegame.juicymatch.game.GameEvent
import com.nativegame.juicymatch.game.GameWorld
import com.nativegame.juicymatch.game.effect.booster.GloveEffect
import com.nativegame.juicymatch.game.layer.tile.Tile
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.juicymatch.game.swap.SwapModifier
import com.nativegame.juicymatch.game.swap.SwapModifier.SwapListener
import com.nativegame.natyengine.engine.Engine

/**
 * Created by Oscar Liang on 2022/02/23
 */
class GloveController(engine: Engine?, tileSystem: TileSystem) :
    BoosterController(engine, tileSystem), SwapListener {
    private val mSwapModifier: SwapModifier
    private val mGloveEffect: GloveEffect

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        mSwapModifier = SwapModifier(engine)
        mSwapModifier.setListener(this)
        mGloveEffect = GloveEffect(engine, Textures.GLOVE)
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun isAddBooster(touchDownTile: Tile, touchUpTile: Tile?): Boolean {
        return touchUpTile != null && touchDownTile.isSwappable() && touchUpTile.isSwappable()
    }

    override fun onAddBooster(
        tiles: Array<Array<Tile?>?>?,
        touchDownTile: Tile,
        touchUpTile: Tile,
        row: Int,
        col: Int
    ) {
        mGloveEffect.activate(
            GameWorld.WORLD_WIDTH / 2f, GameWorld.WORLD_HEIGHT / 2f,
            touchDownTile.getX(), touchDownTile.getY(), touchUpTile.getX(), touchUpTile.getY()
        )
        Sounds.TILE_SLIDE.play()
    }

    override fun onRemoveBooster(
        tiles: Array<Array<Tile>>,
        touchDownTile: Tile,
        touchUpTile: Tile,
        row: Int,
        col: Int
    ) {
        Match3Algorithm.swapTile(tiles as Array<Array<Match3Tile>>, touchDownTile, touchUpTile)
        mSwapModifier.activate(touchDownTile, touchUpTile)
    }

    override fun onSwap(tileA: Tile?, tileB: Tile?) {
        dispatchEvent(GameEvent.PLAYER_USE_BOOSTER)
    } //========================================================
}
