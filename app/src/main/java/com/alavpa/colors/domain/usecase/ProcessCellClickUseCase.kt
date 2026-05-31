package com.alavpa.colors.domain.usecase

import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class CellClickResult {
    data class Success(
        val newBoard: LevelBoard,
        val nextColorToClear: RgbColor?,
        val isLevelCompleted: Boolean
    ) : CellClickResult()
    data object Reset : CellClickResult()
    data object Ignore : CellClickResult()
}

class ProcessCellClickUseCase @Inject constructor() {
    suspend operator fun invoke(
        currentBoard: LevelBoard,
        currentColor: RgbColor?,
        row: Int,
        col: Int
    ): CellClickResult = withContext(Dispatchers.Default) {
        val clickedColor = currentBoard.grid[row][col] ?: return@withContext CellClickResult.Ignore

        if (currentColor != null && currentColor != clickedColor) {
            return@withContext CellClickResult.Reset
        }

        val newGrid = currentBoard.grid.mapIndexed { r, rows ->
            rows.mapIndexed { c, color ->
                if ((r == row) && (c == col)) null else color
            }
        }

        val colorStillExists = newGrid.flatten().contains(clickedColor)
        val allCleared = newGrid.flatten().all { it == null }

        CellClickResult.Success(
            newBoard = currentBoard.copy(grid = newGrid),
            nextColorToClear = if (colorStillExists) clickedColor else null,
            isLevelCompleted = allCleared
        )
    }
}
