package com.nativegame.juicymatch.game.algorithm

import com.nativegame.juicymatch.game.algorithm.special.finder.SpecialTileFinderManager
import com.nativegame.juicymatch.game.layer.tile.Tile
import com.nativegame.juicymatch.game.layer.tile.TileSystem
import com.nativegame.natyengine.engine.Engine
import com.nativegame.natyengine.entity.Entity

/**
 * Created by Oscar Liang on 2022/02/23
 */
abstract class BaseAlgorithm protected constructor(engine: Engine?, tileSystem: TileSystem) :
    Entity(engine), Algorithm {
    @JvmField
    protected val mTiles: Array<Array<Tile>>
    @JvmField
    protected val mTotalRow: Int
    @JvmField
    protected val mTotalCol: Int

    @JvmField
    protected val mSpecialTileFinder: SpecialTileFinderManager

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        mTiles = tileSystem.getChild()
        mTotalRow = tileSystem.getTotalRow()
        mTotalCol = tileSystem.getTotalColumn()
        mSpecialTileFinder = SpecialTileFinderManager(engine)
    } //========================================================
}
