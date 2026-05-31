package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import javax.inject.Inject

class GetHintUseCase @Inject constructor() {
    operator fun invoke(board: LevelBoard, currentColorBeingCleared: RgbColor?): RgbColor? {
        return currentColorBeingCleared ?: board.grid.flatten().filterNotNull().firstOrNull()
    }
}
