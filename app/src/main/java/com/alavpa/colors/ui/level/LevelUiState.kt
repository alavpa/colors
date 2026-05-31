package com.alavpa.colors.ui.level

import com.alavpa.colors.domain.model.LevelBoard
import com.alavpa.colors.domain.model.RgbColor

sealed interface LevelUiState {
    data object Loading : LevelUiState
    data class Success(
        val board: LevelBoard,
        val initialBoard: LevelBoard,
        val currentColorBeingCleared: RgbColor? = null,
        val isAdsRemoved: Boolean = false,
        val remainingHints: Int = 0,
        val showUndoDialog: Boolean = false,
        val hintedColor: RgbColor? = null,
        val isMuted: Boolean = false,
        val showRestartOptions: Boolean = false
    ) : LevelUiState
    data class Error(val message: String) : LevelUiState
}
