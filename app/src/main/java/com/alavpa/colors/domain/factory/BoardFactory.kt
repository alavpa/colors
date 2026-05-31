package com.alavpa.colors.domain.factory

import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import javax.inject.Inject

class BoardFactory @Inject constructor() {
    fun createBoard(level: Level, availableColors: List<RgbColor>): LevelBoard {
        val grid = List(level.verticalSize) {
            List(level.horizontalSize) {
                availableColors.random()
            }
        }

        return LevelBoard.Builder()
            .setLevel(level)
            .setGrid(grid)
            .build()
    }
}
