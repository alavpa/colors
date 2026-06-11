package com.alavpa.colors.domain.logic

import com.alavpa.colors.domain.model.RgbColor
import kotlin.math.abs

/**
 * Contains the business rules to determine color blending and matching logic.
 */
object ColorRules {
    /**
     * Tolerance used to determine if two colors are considered equal in the game.
     */
    private const val MATCH_TOLERANCE = 0.50f

    /**
     * Colors that are not allowed to be used in the game (e.g., surface colors).
     */
    private val FORBIDDEN_COLORS = listOf(
        // Default Material 3 Surface color (Light)
        RgbColor(1.0f, 0.984f, 0.996f),
        // Default Material 3 Surface color (Dark)
        RgbColor(0.109f, 0.105f, 0.121f)
    )

    /**
     * Checks if a color is forbidden.
     */
    fun isForbiddenColor(color: RgbColor): Boolean {
        return FORBIDDEN_COLORS.any { forbidden ->
            isColorMatch(color, forbidden)
        }
    }

    /**
     * Checks if the [current] color matches the [target] color within the defined tolerance.
     */
    fun isColorMatch(current: RgbColor, target: RgbColor): Boolean {
        return abs(current.red - target.red) <= MATCH_TOLERANCE &&
                abs(current.green - target.green) <= MATCH_TOLERANCE &&
                abs(current.blue - target.blue) <= MATCH_TOLERANCE
    }
}
