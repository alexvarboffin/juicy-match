package com.nativegame.juicymatch.algorithm

/**
 * Created by Oscar Liang on 2022/02/23
 */
object Match3Algorithm {
    //========================================================
    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    fun findMatchTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        // Find match 3 in row
        for (i in 0..<row) {
            for (j in 0..<col - 2) {
                // Check is tile type match the next two column
                val type = tiles[i]!![j].getTileType()
                if (type === tiles[i]!![j + 1].getTileType()
                    && type === tiles[i]!![j + 2].getTileType()
                ) {
                    // Update match
                    for (n in 0..2) {
                        val t = tiles[i]!![j + n]
                        // We make sure not match multiple times
                        if (t.isMatchable() && t.getTileState() == TileState.IDLE) {
                            t.matchTile()
                        }
                    }
                }
            }
        }

        // Find match 3 in column
        for (j in 0..<col) {
            for (i in 0..<row - 2) {
                // Check is tile type match the next two row
                val type = tiles[i]!![j].getTileType()
                if (type === tiles[i + 1]!![j].getTileType()
                    && type === tiles[i + 2]!![j].getTileType()
                ) {
                    // Update match
                    for (n in 0..2) {
                        val t = tiles[i + n]!![j]
                        // We make sure not match multiple times
                        if (t.isMatchable() && t.getTileState() == TileState.IDLE) {
                            t.matchTile()
                        }
                    }
                }
            }
        }
    }

    fun playTileEffect(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                if (t.getTileState() == TileState.MATCH) {
                    t.playTileEffect()
                }
            }
        }
    }

    fun resetMatchTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        // Reset tile row and column
        for (j in 0..<col) {
            for (i in row - 1 downTo 0) {
                val currentTile = tiles[i]!![j]
                // Find the match tile
                if (currentTile.getTileState() != TileState.MATCH) {
                    continue
                }
                // Search the column bottom up
                for (n in i - 1 downTo 0) {
                    val upperTile = tiles[n]!![j]
                    // We skip the negligible tile
                    if (upperTile.isNegligible()) {
                        continue
                    }
                    // We first check if upper tile is swappable
                    if (!upperTile.isSwappable()) {
                        // Put it to waiting state and hide it before reset
                        currentTile.setTileState(TileState.WAITING)
                        currentTile.hideTile()
                        break
                    }
                    // Otherwise, swap with the idle upper tile
                    if (upperTile.getTileState() == TileState.IDLE) {
                        swapTile(tiles, currentTile, upperTile)
                        break
                    }
                }
            }
        }

        // Reset tile position and type
        for (j in 0..<col) {
            var i = row - 1
            var n = 1
            while (i >= 0) {
                val t = tiles[i]!![j]
                // Find the match tile
                if (t.getTileState() == TileState.MATCH) {
                    t.resetXByColumn(t.getColumn())
                    t.resetYByRow(-n++)
                    t.resetTile()
                }
                i--
            }
        }
    }

    fun findUnreachableTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        // Important to search from top down to left right
        for (i in 1..<row) {
            for (j in 0..<col) {
                val currentTile = tiles[i]!![j]
                // Find the waiting tile
                if (currentTile.getTileState() != TileState.WAITING) {
                    continue
                }
                // Search the column bottom up
                for (n in i - 1 downTo 0) {
                    val upperTile = tiles[n]!![j]
                    // We skip the negligible tile
                    if (upperTile.isNegligible()) {
                        continue
                    }
                    // Check is the 3 upper tile swappable
                    //  X X X
                    //    O
                    //    O
                    if ((j == 0 || !tiles[n]!![j - 1].isSwappable())
                        && !tiles[n]!![j].isSwappable() && (j == col - 1 || !tiles[n]!![j + 1].isSwappable())
                    ) {
                        // The tile is not reachable, we put it to unreachable state
                        currentTile.setTileState(TileState.UNREACHABLE)
                    }

                    // No need to check the next one if it is reachable
                    break
                }
            }
        }
    }

    fun checkUnreachableTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        // Important to search from top down to left right
        for (i in 1..<row) {
            for (j in 0..<col) {
                val currentTile = tiles[i]!![j]
                // Find the unreachable tile
                if (currentTile.getTileState() != TileState.UNREACHABLE) {
                    continue
                }
                // Search the column bottom up
                for (n in i - 1 downTo 0) {
                    val upperTile = tiles[n]!![j]
                    // We skip the negligible tile
                    if (upperTile.isNegligible()) {
                        continue
                    }
                    // Check is the 3 upper tile swappable
                    //  X X X
                    //    O
                    //    O
                    if ((j > 0 && tiles[n]!![j - 1].isSwappable())
                        || tiles[n]!![j].isSwappable()
                        || (j < col - 1 && tiles[n]!![j + 1].isSwappable())
                    ) {
                        // The tile is now reachable, we put it to match state
                        currentTile.setTileState(TileState.MATCH)
                    }

                    // No need to check the next one if it is unreachable
                    break
                }
            }
        }
    }

    fun checkWaitingTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        for (j in 0..<col) {
            for (i in row - 1 downTo 1) {
                val currentTile = tiles[i]!![j]
                // Find the waiting tile
                if (currentTile.getTileState() != TileState.WAITING) {
                    continue
                }
                // Search the column bottom up
                for (n in i - 1 downTo 0) {
                    val upperTile = tiles[n]!![j]
                    // We skip the negligible tile
                    if (upperTile.isNegligible()) {
                        continue
                    }
                    // Find the unswappable tile
                    if (!upperTile.isSwappable()) {
                        // Search the right one next to it
                        //    X O
                        //    O
                        //    O
                        if (j < col - 1) {
                            val targetTile = tiles[n]!![j + 1]
                            // We make sure the tile is not unswappable or still moving
                            if (targetTile.isSwappable()
                                && !targetTile.isMoving() && targetTile.getTileState() != TileState.WAITING
                            ) {
                                // Put the tile to match state and swap
                                currentTile.setTileState(TileState.MATCH)
                                swapTile(tiles, targetTile, currentTile)
                                // We find available tile from right, so we do not search left
                                break
                            }
                        }

                        // Search the left one next to it
                        //  O X
                        //    O
                        //    O
                        if (j > 0) {
                            val targetTile = tiles[n]!![j - 1]
                            // We make sure the tile is not unswappable or still moving
                            if (targetTile.isSwappable()
                                && !targetTile.isMoving() && targetTile.getTileState() != TileState.WAITING
                            ) {
                                // Put the tile to match state and swap
                                currentTile.setTileState(TileState.MATCH)
                                swapTile(tiles, targetTile, currentTile)
                            }
                        }

                        // No need to find the next unswappable tile
                        break
                    }
                }
            }
        }
    }

    fun swapTile(tiles: Array<Array<Match3Tile>>, tileA: Match3Tile, tileB: Match3Tile) {
        // Swap row
        val rowA = tileA.getRow()
        val rowB = tileB.getRow()
        tileA.setRow(rowB)
        tileB.setRow(rowA)

        // Swap column
        val colA = tileA.getColumn()
        val colB = tileB.getColumn()
        tileA.setColumn(colB)
        tileB.setColumn(colA)

        // Swap tile
        tiles[rowA]!![colA] = tileB
        tiles[rowB]!![colB] = tileA
    }

    fun initTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                t.initTile()
            }
        }
    }

    fun shuffleTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int) {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i][j]
                if (t.isShufflable()) {
                    t.shuffleTile()
                }
            }
        }
    }

    fun moveTile(tiles: Array<Array<Match3Tile>>, row: Int, col: Int, elapsedMillis: Long) {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                t.moveTile(elapsedMillis)
            }
        }
    }

    fun isMatch(tiles: Array<Array<Match3Tile>>, row: Int, col: Int): Boolean {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                if (t.getTileState() == TileState.MATCH) {
                    return true
                }
            }
        }

        return false
    }

    fun isWaiting(tiles: Array<Array<Match3Tile>>, row: Int, col: Int): Boolean {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                if (t.getTileState() == TileState.WAITING) {
                    return true
                }
            }
        }

        return false
    }

    fun isMoving(tiles: Array<Array<Match3Tile>>, row: Int, col: Int): Boolean {
        for (i in 0..<row) {
            for (j in 0..<col) {
                val t = tiles[i]!![j]
                if (t.isMoving()) {
                    return true
                }
            }
        }

        return false
    } //========================================================
}
