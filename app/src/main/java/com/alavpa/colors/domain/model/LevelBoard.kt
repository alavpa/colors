package com.alavpa.colors.domain.model

data class LevelBoard(
    val level: Level,
    val grid: List<List<RgbColor?>>
) {
    class Builder {
        private var level: Level? = null
        private var grid: List<List<RgbColor?>>? = null

        fun setLevel(level: Level) = apply { this.level = level }
        fun setGrid(grid: List<List<RgbColor?>>) = apply { this.grid = grid }

        fun build(): LevelBoard {
            return LevelBoard(
                level = checkNotNull(level) { "Level must be set" },
                grid = checkNotNull(grid) { "Grid must be set" }
            )
        }
    }
}
