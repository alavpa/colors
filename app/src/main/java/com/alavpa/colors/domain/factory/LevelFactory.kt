package com.alavpa.colors.domain.factory

import com.alavpa.colors.domain.model.GameRules
import com.alavpa.colors.domain.model.Level
import javax.inject.Inject

class LevelFactory @Inject constructor() {
    fun createLevel(levelId: Int): Level {
        var cols = (levelId + 2) / 2 + 1
        var rows = (levelId + 1) / 2 + 1

        if (cols > GameRules.MAX_COLS) {
            val extra = cols - GameRules.MAX_COLS
            cols = GameRules.MAX_COLS
            rows += extra
        }

        if (rows > GameRules.MAX_ROWS) {
            rows = GameRules.MAX_ROWS
        }

        val cellCount = rows * cols
        val colorCount = (levelId + 1).coerceAtMost(cellCount)

        return Level(
            id = levelId,
            cellCount = cellCount,
            colorCount = colorCount,
            verticalSize = rows,
            horizontalSize = cols
        )
    }
}
