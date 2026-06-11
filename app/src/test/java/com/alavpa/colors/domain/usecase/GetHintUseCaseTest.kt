package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetHintUseCaseTest {

    private val useCase = GetHintUseCase()
    private val red = RgbColor(1f, 0f, 0f)
    private val blue = RgbColor(0f, 0f, 1f)
    private val level = Level(1, 4, 2, 2, 2)

    @Test
    fun `invoke should return current color if provided`() {
        val board = LevelBoard(level, listOf(listOf(red, blue), listOf(blue, red)))

        val result = useCase(board, blue)

        assertEquals(blue, result)
    }

    @Test
    fun `invoke should return first non-null color if no current color provided`() {
        val board = LevelBoard(level, listOf(listOf(null, blue), listOf(red, null)))

        val result = useCase(board, null)

        assertEquals(blue, result)
    }

    @Test
    fun `invoke should return null if board is empty`() {
        val board = LevelBoard(level, listOf(listOf(null, null), listOf(null, null)))

        val result = useCase(board, null)

        assertNull(result)
    }
}
