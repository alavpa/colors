package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.factory.BoardFactory
import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.repository.ColorRepository
import com.alavpa.colors.domain.repository.LevelRepository
import javax.inject.Inject

class GetLevelUseCase @Inject constructor(
    private val levelRepository: LevelRepository,
    private val colorRepository: ColorRepository,
    private val boardFactory: BoardFactory
) {
    operator fun invoke(levelId: Int): LevelBoard {
        val level = levelRepository.getLevel(levelId)

        val availableColors = List(level.colorCount) {
            colorRepository.getRandomColor()
        }

        return boardFactory.createBoard(level, availableColors)
    }
}
