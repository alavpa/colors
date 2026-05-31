package com.alavpa.colors.domain.factory

import com.alavpa.colors.domain.model.Level
import javax.inject.Inject

class LevelFactory @Inject constructor() {
    fun createLevel(levelId: Int): Level {
        val maxCols = 5
        val maxRows = 10
        var cols = (levelId + 1) / 2 + 1
        var rows = levelId / 2 + 1

        if (cols > maxCols) {
            val extra = cols - maxCols
            cols = maxCols
            rows += extra
        }

        if (rows > maxRows) {
            rows = maxRows
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
