package com.alavpa.colors.domain.factory

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelFactoryTest {

    private val factory = LevelFactory()

    @Test
    fun `level 1 should have 2x2 grid and 2 colors`() {
        val level = factory.createLevel(1)
        assertEquals(2, level.verticalSize)
        assertEquals(2, level.horizontalSize)
        assertEquals(2, level.colorCount)
    }

    @Test
    fun `level 13 should reach max grid size 10x5`() {
        val level = factory.createLevel(13)
        assertEquals(10, level.verticalSize)
        assertEquals(5, level.horizontalSize)
        assertEquals(14, level.colorCount)
    }

    @Test
    fun `level 50 should stay at max grid size 10x5 and cap colors`() {
        val level = factory.createLevel(50)
        assertEquals(10, level.verticalSize)
        assertEquals(5, level.horizontalSize)
        assertEquals(50, level.colorCount)
    }

    @Test
    fun `color count should never exceed cell count`() {
        for (i in 1..100) {
            val level = factory.createLevel(i)
            assertTrue(
                "Color count ${level.colorCount} exceeds cell count ${level.cellCount} at level $i",
                level.colorCount <= level.cellCount
            )
        }
    }
}
