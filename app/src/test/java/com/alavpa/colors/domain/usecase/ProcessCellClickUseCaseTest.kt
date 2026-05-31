package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProcessCellClickUseCaseTest {

    private val useCase = ProcessCellClickUseCase()
    private val red = RgbColor(1f, 0f, 0f)
    private val blue = RgbColor(0f, 0f, 1f)
    private val level = Level(1, 4, 2, 2, 2)

    @Test
    fun `clicking a color for the first time should succeed and set next color`() = runBlocking {
        val grid = listOf(listOf(red, blue), listOf(blue, red))
        val board = LevelBoard(level, grid)

        val result = useCase(board, null, 0, 0)

        assertTrue(result is CellClickResult.Success)
        val success = result as CellClickResult.Success
        assertNull(success.newBoard.grid[0][0])
        assertEquals(red, success.nextColorToClear)
    }

    @Test
    fun `clicking a wrong color should trigger reset`() = runBlocking {
        val grid = listOf(listOf(red, blue), listOf(blue, red))
        val board = LevelBoard(level, grid)

        val result = useCase(board, red, 0, 1) // 0,1 is blue

        assertTrue(result is CellClickResult.Reset)
    }

    @Test
    fun `clearing last cell of a color should clear nextColorToClear`() = runBlocking {
        val grid = listOf(listOf(red, blue), listOf(blue, null))
        val board = LevelBoard(level, grid)

        val result = useCase(board, red, 0, 0)

        assertTrue(result is CellClickResult.Success)
        val success = result as CellClickResult.Success
        assertNull(success.nextColorToClear)
    }

    @Test
    fun `clearing all cells should return isLevelCompleted true`() = runBlocking {
        val grid = listOf(listOf(red, null), listOf(null, null))
        val board = LevelBoard(level, grid)

        val result = useCase(board, red, 0, 0)

        assertTrue(result is CellClickResult.Success)
        val success = result as CellClickResult.Success
        assertTrue(success.isLevelCompleted)
    }
}
