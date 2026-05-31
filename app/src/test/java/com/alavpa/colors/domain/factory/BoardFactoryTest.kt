package com.alavpa.colors.domain.factory

import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.model.RgbColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardFactoryTest {

    private val factory = BoardFactory()

    @Test
    fun `createBoard should generate grid with correct dimensions`() {
        val level = Level(1, 4, 2, 2, 2)
        val colors = listOf(RgbColor(1f, 0f, 0f), RgbColor(0f, 1f, 0f))

        val board = factory.createBoard(level, colors)

        assertEquals(2, board.grid.size)
        assertEquals(2, board.grid[0].size)
    }

    @Test
    fun `createBoard should only use provided colors`() {
        val level = Level(1, 4, 2, 2, 2)
        val colors = listOf(RgbColor(1f, 0f, 0f), RgbColor(0f, 1f, 0f))

        val board = factory.createBoard(level, colors)

        board.grid.flatten().forEach { cell ->
            assertTrue(colors.contains(cell))
        }
    }
}
