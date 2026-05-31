package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.repository.ColorRepository
import com.alavpa.colors.domain.repository.LevelRepository
import javax.inject.Inject

class GetLevelUseCase @Inject constructor(
    private val levelRepository: LevelRepository,
    private val colorRepository: ColorRepository
) {
    operator fun invoke(levelId: Int): LevelBoard {
        val level = levelRepository.getLevel(levelId)
        
        // Get the set of colors available for this level
        val availableColors = List(level.colorCount) {
            colorRepository.getRandomColor()
        }

        // Create the matrix (grid) and assign a random color from the available set to each cell
        val grid = List(level.verticalSize) {
            List(level.horizontalSize) {
                availableColors.random()
            }
        }

        return LevelBoard(
            level = level,
            grid = grid
        )
    }
}
