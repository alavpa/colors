package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.factory.BoardFactory
import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import com.alavpa.colors.domain.repository.ColorRepository
import com.alavpa.colors.domain.repository.LevelRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetLevelUseCaseTest {

    private val levelRepository: LevelRepository = mockk()
    private val colorRepository: ColorRepository = mockk()
    private val boardFactory: BoardFactory = mockk()
    private val useCase = GetLevelUseCase(levelRepository, colorRepository, boardFactory)

    private val level = Level(
        id = 1,
        cellCount = 4,
        colorCount = 2,
        verticalSize = 2,
        horizontalSize = 2
    )

    private val color1 = RgbColor(1f, 0f, 0f) // Red
    private val color2 = RgbColor(0f, 1f, 0f) // Green
    private val forbiddenColor = RgbColor(1.0f, 0.984f, 0.996f) // Surface Light
    private val similarToColor1 = RgbColor(0.99f, 0f, 0f) // Very similar to Red

    @Test
    fun `invoke should return board with correct colors`() = runBlocking {
        // Given
        every { levelRepository.getLevel(1) } returns level
        every { colorRepository.getRandomColor() } returnsMany listOf(color1, color2)
        val expectedBoard =
            LevelBoard(level, listOf(listOf(color1, color2), listOf(color2, color1)))
        every { boardFactory.createBoard(level, any()) } returns expectedBoard

        // When
        val result = useCase(1)

        // Then
        assertEquals(expectedBoard, result)
        verify { boardFactory.createBoard(level, listOf(color1, color2)) }
    }

    @Test
    fun `invoke should discard forbidden colors`() = runBlocking {
        // Given
        every { levelRepository.getLevel(1) } returns level
        // Forbidden color followed by valid colors
        every { colorRepository.getRandomColor() } returnsMany listOf(
            forbiddenColor,
            color1,
            color2
        )
        val expectedBoard =
            LevelBoard(level, listOf(listOf(color1, color2), listOf(color2, color1)))
        every { boardFactory.createBoard(level, any()) } returns expectedBoard

        // When
        val result = useCase(1)

        // Then
        assertEquals(expectedBoard, result)
        verify { boardFactory.createBoard(level, listOf(color1, color2)) }
    }

    @Test
    fun `invoke should discard duplicate colors`() = runBlocking {
        // Given
        every { levelRepository.getLevel(1) } returns level
        // Duplicate color1 (or similar) followed by valid colors
        every { colorRepository.getRandomColor() } returnsMany listOf(
            color1,
            similarToColor1,
            color2
        )
        val expectedBoard =
            LevelBoard(level, listOf(listOf(color1, color2), listOf(color2, color1)))
        every { boardFactory.createBoard(level, any()) } returns expectedBoard

        // When
        val result = useCase(1)

        // Then
        assertEquals(expectedBoard, result)
        verify { boardFactory.createBoard(level, listOf(color1, color2)) }
    }

    @Test
    fun `invoke should use fallback if max attempts reached`() = runBlocking {
        // Given
        val levelWithManyColors = level.copy(colorCount = 2)
        every { levelRepository.getLevel(1) } returns levelWithManyColors

        // Always return forbidden color to force max attempts
        every { colorRepository.getRandomColor() } returns forbiddenColor

        val expectedBoard = LevelBoard(
            levelWithManyColors,
            listOf(listOf(forbiddenColor, forbiddenColor), listOf(forbiddenColor, forbiddenColor))
        )
        every { boardFactory.createBoard(levelWithManyColors, any()) } returns expectedBoard

        // When
        val result = useCase(1)

        // Then
        assertEquals(expectedBoard, result)
        // availableColors will eventually be filled by the fallback loop
        verify {
            boardFactory.createBoard(
                levelWithManyColors,
                listOf(forbiddenColor, forbiddenColor)
            )
        }
    }
}
