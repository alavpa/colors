package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.factory.BoardFactory
import com.alavpa.colors.domain.logic.ColorRules
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import com.alavpa.colors.domain.repository.ColorRepository
import com.alavpa.colors.domain.repository.LevelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLevelUseCase @Inject constructor(
    private val levelRepository: LevelRepository,
    private val colorRepository: ColorRepository,
    private val boardFactory: BoardFactory
) {
    suspend operator fun invoke(levelId: Int): LevelBoard = withContext(Dispatchers.Default) {
        val level = levelRepository.getLevel(levelId)

        val availableColors = mutableListOf<RgbColor>()
        var attempts = 0
        val maxAttempts = level.colorCount * 10

        while (availableColors.size < level.colorCount && attempts < maxAttempts) {
            val color = colorRepository.getRandomColor()
            val isForbidden = ColorRules.isForbiddenColor(color)
            val isDuplicate = availableColors.any {
                ColorRules.isColorMatch(color, it)
            }

            if (!isForbidden && !isDuplicate) {
                availableColors.add(color)
            }
            attempts++
        }

        // Fallback if we couldn't generate enough unique colors (should be rare)
        while (availableColors.size < level.colorCount) {
            availableColors.add(colorRepository.getRandomColor())
        }

        boardFactory.createBoard(level, availableColors)
    }
}
