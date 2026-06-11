package com.alavpa.colors.domain.logic

import com.alavpa.colors.domain.model.RgbColor
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorRulesTest {

    @Test
    fun `isColorMatch should return true when colors are within tolerance`() {
        val color1 = RgbColor(0.5f, 0.5f, 0.5f)
        val color2 = RgbColor(0.6f, 0.4f, 0.55f) // All within 0.50f tolerance

        assertTrue(ColorRules.isColorMatch(color1, color2))
    }

    @Test
    fun `isColorMatch should return false when red is outside tolerance`() {
        val color1 = RgbColor(0.1f, 0.5f, 0.5f)
        val color2 = RgbColor(0.7f, 0.5f, 0.5f) // Red diff is 0.6, tolerance is 0.5

        assertFalse(ColorRules.isColorMatch(color1, color2))
    }

    @Test
    fun `isForbiddenColor should return true for surface light color`() {
        val surfaceLight = RgbColor(1.0f, 0.984f, 0.996f)
        assertTrue(ColorRules.isForbiddenColor(surfaceLight))
    }

    @Test
    fun `isForbiddenColor should return true for colors similar to surface dark`() {
        val similarToSurfaceDark = RgbColor(0.12f, 0.11f, 0.13f)
        assertTrue(ColorRules.isForbiddenColor(similarToSurfaceDark))
    }

    @Test
    fun `isForbiddenColor should return false for very different colors`() {
        val vibrantRed = RgbColor(1.0f, 0.0f, 0.0f)
        assertFalse(ColorRules.isForbiddenColor(vibrantRed))
    }
}
