package com.puzzle.app.engine

data class PuzzleState(
    val tiles: List<Int>,
    val rows: Int,
    val cols: Int,
    val emptyIndex: Int
) {
    val totalTiles get() = rows * cols

    fun isSolved(): Boolean {
        for (i in 0 until totalTiles - 1) {
            if (tiles[i] != i) return false
        }
        return tiles[totalTiles - 1] == totalTiles - 1
    }

    fun getTilePosition(index: Int): Pair<Int, Int> {
        return Pair(index / cols, index % cols)
    }

    fun canMove(index: Int): Boolean {
        val emptyRow = emptyIndex / cols
        val emptyCol = emptyIndex % cols
        val tileRow = index / cols
        val tileCol = index % cols

        if (index == emptyIndex) return false

        return (tileRow == emptyRow && kotlin.math.abs(tileCol - emptyCol) == 1) ||
               (tileCol == emptyCol && kotlin.math.abs(tileRow - emptyRow) == 1)
    }

    fun move(index: Int): PuzzleState? {
        if (!canMove(index)) return null
        val newTiles = tiles.toMutableList()
        newTiles[emptyIndex] = tiles[index]
        newTiles[index] = tiles[emptyIndex]
        return PuzzleState(newTiles, rows, cols, index)
    }
}

object PuzzleEngine {

    fun createSolvedState(rows: Int, cols: Int): PuzzleState {
        val tiles = (0 until rows * cols).toList()
        return PuzzleState(tiles, rows, cols, rows * cols - 1)
    }

    fun shuffle(state: PuzzleState, maxMoves: Int = 1000): PuzzleState {
        var current = state
        val random = java.util.Random()

        for (i in 0 until maxMoves) {
            val neighbors = getMovableIndices(current)
            if (neighbors.isNotEmpty()) {
                val randomIndex = neighbors[random.nextInt(neighbors.size)]
                current = current.move(randomIndex) ?: current
            }
        }

        if (current.isSolved() || !isSolvable(current)) {
            return shuffle(state, maxMoves)
        }

        return current
    }

    private fun getMovableIndices(state: PuzzleState): List<Int> {
        return (0 until state.totalTiles).filter { state.canMove(it) }
    }

    fun isSolvable(state: PuzzleState): Boolean {
        val tiles = state.tiles
        val n = state.totalTiles
        val emptyRow = state.emptyIndex / state.cols

        var inversions = 0
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                if (tiles[i] != n - 1 && tiles[j] != n - 1 && tiles[i] > tiles[j]) {
                    inversions++
                }
            }
        }

        return if (state.cols % 2 == 0) {
            val emptyFromBottom = state.rows - emptyRow
            if (emptyFromBottom % 2 == 0) {
                inversions % 2 == 1
            } else {
                inversions % 2 == 0
            }
        } else {
            inversions % 2 == 0
        }
    }

    fun countInversions(tiles: List<Int>): Int {
        var inversions = 0
        val n = tiles.size
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                if (tiles[i] != n - 1 && tiles[j] != n - 1 && tiles[i] > tiles[j]) {
                    inversions++
                }
            }
        }
        return inversions
    }
}
