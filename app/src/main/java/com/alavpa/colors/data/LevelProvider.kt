package com.alavpa.colors.data

import com.alavpa.colors.domain.model.Level
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LevelProvider @Inject constructor() {
    fun getLevel(levelId: Int): Level {
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
        // Ensure color count doesn't exceed total cells
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
