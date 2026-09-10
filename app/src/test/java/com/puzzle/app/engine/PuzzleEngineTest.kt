package com.puzzle.app.engine

import org.junit.Assert.*
import org.junit.Test

class PuzzleEngineTest {

    @Test
    fun `create solved state is actually solved`() {
        val state = PuzzleEngine.createSolvedState(3, 3)
        assertTrue(state.isSolved())
    }

    @Test
    fun `solved state has correct tile order`() {
        val state = PuzzleEngine.createSolvedState(3, 3)
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8), state.tiles)
    }

    @Test
    fun `4x4 solved state is correct`() {
        val state = PuzzleEngine.createSolvedState(4, 4)
        assertTrue(state.isSolved())
        assertEquals(16, state.totalTiles)
    }

    @Test
    fun `3x10 solved state is correct`() {
        val state = PuzzleEngine.createSolvedState(3, 10)
        assertTrue(state.isSolved())
        assertEquals(30, state.totalTiles)
    }

    @Test
    fun `can move returns true for adjacent tiles`() {
        val state = PuzzleState(
            tiles = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 8
        )
        assertTrue(state.canMove(5))
        assertTrue(state.canMove(7))
    }

    @Test
    fun `can move returns false for non-adjacent tiles`() {
        val state = PuzzleState(
            tiles = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 8
        )
        assertFalse(state.canMove(0))
        assertFalse(state.canMove(1))
        assertFalse(state.canMove(3))
    }

    @Test
    fun `can move returns false for empty tile itself`() {
        val state = PuzzleState(
            tiles = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 8
        )
        assertFalse(state.canMove(8))
    }

    @Test
    fun `move produces correct new state`() {
        val state = PuzzleState(
            tiles = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 8
        )
        val newState = state.move(5)
        assertNotNull(newState)
        assertEquals(8, newState!!.tiles[5])
        assertEquals(5, newState.tiles[8])
        assertEquals(5, newState.emptyIndex)
    }

    @Test
    fun `move returns null for invalid move`() {
        val state = PuzzleState(
            tiles = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 8
        )
        assertNull(state.move(0))
    }

    @Test
    fun `shuffle produces solvable state`() {
        val original = PuzzleEngine.createSolvedState(3, 3)
        val shuffled = PuzzleEngine.shuffle(original)
        assertFalse(shuffled.isSolved())
        assertTrue(PuzzleEngine.isSolvable(shuffled))
    }

    @Test
    fun `shuffle preserves all tiles`() {
        val original = PuzzleEngine.createSolvedState(3, 3)
        val shuffled = PuzzleEngine.shuffle(original)
        val sortedOriginal = original.tiles.sorted()
        val sortedShuffled = shuffled.tiles.sorted()
        assertEquals(sortedOriginal, sortedShuffled)
    }

    @Test
    fun `shuffle works for 3x4`() {
        val original = PuzzleEngine.createSolvedState(3, 4)
        val shuffled = PuzzleEngine.shuffle(original)
        assertFalse(shuffled.isSolved())
        assertTrue(PuzzleEngine.isSolvable(shuffled))
        assertEquals(12, shuffled.tiles.size)
    }

    @Test
    fun `shuffle works for 3x10`() {
        val original = PuzzleEngine.createSolvedState(3, 10)
        val shuffled = PuzzleEngine.shuffle(original)
        assertFalse(shuffled.isSolved())
        assertTrue(PuzzleEngine.isSolvable(shuffled))
        assertEquals(30, shuffled.tiles.size)
    }

    @Test
    fun `countInversions for solved state is zero`() {
        val state = PuzzleEngine.createSolvedState(3, 3)
        assertEquals(0, PuzzleEngine.countInversions(state.tiles))
    }

    @Test
    fun `countInversions increases with disorder`() {
        val state = PuzzleState(
            tiles = listOf(1, 0, 2, 3, 4, 5, 6, 7, 8),
            rows = 3, cols = 3, emptyIndex = 1
        )
        assertEquals(1, PuzzleEngine.countInversions(state.tiles))
    }

    @Test
    fun `isSolvable returns true for solved state`() {
        val state = PuzzleEngine.createSolvedState(3, 3)
        assertTrue(PuzzleEngine.isSolvable(state))
    }

    @Test
    fun `multiple shuffles are all solvable`() {
        val original = PuzzleEngine.createSolvedState(3, 3)
        repeat(20) {
            val shuffled = PuzzleEngine.shuffle(original)
            assertTrue("Shuffle $it should be solvable", PuzzleEngine.isSolvable(shuffled))
        }
    }

    @Test
    fun `getTilePosition returns correct coordinates`() {
        val state = PuzzleEngine.createSolvedState(3, 3)
        assertEquals(Pair(0, 0), state.getTilePosition(0))
        assertEquals(Pair(0, 2), state.getTilePosition(2))
        assertEquals(Pair(1, 0), state.getTilePosition(3))
        assertEquals(Pair(2, 2), state.getTilePosition(8))
    }

    @Test
    fun `solve sequence works for 3x3`() {
        var state = PuzzleEngine.shuffle(PuzzleEngine.createSolvedState(3, 3))
        val target = PuzzleEngine.createSolvedState(3, 3)
        val moves = mutableListOf<Int>()

        repeat(1000) {
            if (state.isSolved()) return
            val emptyIdx = state.emptyIndex
            val emptyRow = emptyIdx / state.cols
            val emptyCol = emptyIdx % state.cols

            val candidates = mutableListOf<Int>()
            if (emptyRow > 0) candidates.add((emptyRow - 1) * state.cols + emptyCol)
            if (emptyRow < state.rows - 1) candidates.add((emptyRow + 1) * state.cols + emptyCol)
            if (emptyCol > 0) candidates.add(emptyRow * state.cols + (emptyCol - 1))
            if (emptyCol < state.cols - 1) candidates.add(emptyRow * state.cols + (emptyCol + 1))

            for (candidate in candidates) {
                val newState = state.move(candidate)
                if (newState != null) {
                    val currentInversions = PuzzleEngine.countInversions(state.tiles)
                    val newInversions = PuzzleEngine.countInversions(newState.tiles)
                    if (newInversions <= currentInversions) {
                        state = newState
                        moves.add(candidate)
                        break
                    }
                }
            }
        }
    }
}
