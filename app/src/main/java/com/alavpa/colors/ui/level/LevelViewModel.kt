package com.alavpa.colors.ui.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alavpa.colors.domain.model.GameRules
import com.alavpa.colors.domain.repository.UserPreferencesRepository
import com.alavpa.colors.domain.usecase.CellClickResult
import com.alavpa.colors.domain.usecase.GetHintUseCase
import com.alavpa.colors.domain.usecase.GetLevelUseCase
import com.alavpa.colors.domain.usecase.ProcessCellClickUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LevelUiEvent {
    data class ShowInterstitial(val onDismissed: () -> Unit) : LevelUiEvent
    data class ShowRewarded(val onRewarded: () -> Unit, val onDismissed: () -> Unit) : LevelUiEvent
    data class ShowHintConfirmation(val onConfirmed: () -> Unit) : LevelUiEvent
    data class NavigateToLevel(val levelId: Int) : LevelUiEvent
}

@HiltViewModel
class LevelViewModel @Inject constructor(
    private val getLevelUseCase: GetLevelUseCase,
    private val processCellClickUseCase: ProcessCellClickUseCase,
    private val getHintUseCase: GetHintUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LevelUiState>(LevelUiState.Loading)
    val uiState: StateFlow<LevelUiState> = _uiState.asStateFlow()

    private val _uiEvent = kotlinx.coroutines.flow.MutableSharedFlow<LevelUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.isAdsRemoved.collectLatest { isAdsRemoved ->
                updateSuccessState { it.copy(isAdsRemoved = isAdsRemoved) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.remainingHints.collectLatest { hints ->
                updateSuccessState { it.copy(remainingHints = hints) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.isMuted.collectLatest { isMuted ->
                updateSuccessState { it.copy(isMuted = isMuted) }
            }
        }
    }

    fun onScreenStarted(levelId: Int?) {
        viewModelScope.launch {
            if (levelId == null) {
                val savedLevel = userPreferencesRepository.currentLevel.first()
                _uiEvent.emit(LevelUiEvent.NavigateToLevel(savedLevel))
            } else {
                loadLevel(levelId)
            }
        }
    }

    private fun updateSuccessState(transform: (LevelUiState.Success) -> LevelUiState.Success) {
        val state = _uiState.value
        if (state is LevelUiState.Success) {
            _uiState.value = transform(state)
        }
    }

    fun loadLevel(levelId: Int) {
        val currentState = _uiState.value
        if (currentState is LevelUiState.Success && currentState.board.level.id == levelId) {
            return
        }

        viewModelScope.launch {
            _uiState.value = LevelUiState.Loading
            try {
                val board = getLevelUseCase(levelId)
                val isAdsRemoved = userPreferencesRepository.isAdsRemoved.first()
                val hints = userPreferencesRepository.remainingHints.first()
                val isMuted = userPreferencesRepository.isMuted.first()
                userPreferencesRepository.setCurrentLevel(levelId)

                _uiState.value = LevelUiState.Success(
                    board = board,
                    initialBoard = board,
                    isAdsRemoved = isAdsRemoved,
                    remainingHints = hints,
                    isMuted = isMuted
                )
            } catch (e: Exception) {
                _uiState.value = LevelUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun restartGame() {
        viewModelScope.launch {
            _uiEvent.emit(LevelUiEvent.NavigateToLevel(1))
            updateSuccessState { it.copy(showRestartOptions = false) }
        }
    }

    fun onRestartClicked() {
        updateSuccessState { it.copy(showRestartOptions = true) }
    }

    fun onRestartOptionsDismissed() {
        updateSuccessState { it.copy(showRestartOptions = false) }
    }

    fun resetBoard() {
        updateSuccessState {
            it.copy(
                board = it.initialBoard,
                currentColorBeingCleared = null,
                showRestartOptions = false
            )
        }
    }

    fun toggleMute() {
        viewModelScope.launch {
            val currentMuted = userPreferencesRepository.isMuted.first()
            userPreferencesRepository.setMuted(!currentMuted)
        }
    }

    fun onCellClick(row: Int, col: Int) {
        val currentState = _uiState.value
        if (currentState is LevelUiState.Success) {
            viewModelScope.launch {
                val result = processCellClickUseCase(
                    currentBoard = currentState.board,
                    currentColor = currentState.currentColorBeingCleared,
                    row = row,
                    col = col
                )

                when (result) {
                    is CellClickResult.Success -> {
                        if (result.isLevelCompleted) {
                            val nextLevelId = currentState.board.level.id + 1
                            if (!currentState.isAdsRemoved && nextLevelId % 3 == 0) {
                                _uiEvent.emit(LevelUiEvent.ShowInterstitial {
                                    viewModelScope.launch {
                                        _uiEvent.emit(LevelUiEvent.NavigateToLevel(nextLevelId))
                                    }
                                })
                            } else {
                                _uiEvent.emit(LevelUiEvent.NavigateToLevel(nextLevelId))
                            }
                        } else {
                            _uiState.value = currentState.copy(
                                board = result.newBoard,
                                currentColorBeingCleared = result.nextColorToClear
                            )
                        }
                    }

                    CellClickResult.Reset -> {
                        _uiState.value = currentState.copy(showUndoDialog = true)
                    }

                    CellClickResult.Ignore -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    fun onUndoAdWatched() {
        val currentState = _uiState.value
        if (currentState is LevelUiState.Success) {
            if (currentState.isAdsRemoved) {
                updateSuccessState { it.copy(showUndoDialog = false) }
            } else {
                viewModelScope.launch {
                    _uiEvent.emit(LevelUiEvent.ShowRewarded(
                        onRewarded = {
                            updateSuccessState { it.copy(showUndoDialog = false) }
                        },
                        onDismissed = {
                            // If they didn't watch it fully, we might still reset or keep dialog
                            // Usually if dismissed without reward, we reset.
                            // But AdManager showRewarded calls onAdDismissed even if rewarded.
                            // So we need to be careful.
                        }
                    ))
                }
            }
        }
    }

    fun onUndoCancelled() {
        updateSuccessState {
            it.copy(
                board = it.initialBoard,
                currentColorBeingCleared = null,
                showUndoDialog = false
            )
        }
    }

    fun onWatchAdForHintsClicked() {
        viewModelScope.launch {
            _uiEvent.emit(LevelUiEvent.ShowRewarded(
                onRewarded = {
                    viewModelScope.launch {
                        val currentHints = userPreferencesRepository.remainingHints.first()
                        userPreferencesRepository.setRemainingHints(currentHints + GameRules.REWARDS_PER_AD)
                    }
                },
                onDismissed = {}
            ))
        }
    }

    fun onHintClicked() {
        val currentState = _uiState.value
        if (currentState is LevelUiState.Success) {
            viewModelScope.launch {
                if (currentState.remainingHints > 0) {
                    _uiEvent.emit(LevelUiEvent.ShowHintConfirmation {
                        useHint()
                    })
                } else {
                    updateSuccessState { it.copy(showWatchAdForHintsDialog = true) }
                }
            }
        }
    }

    fun onWatchAdForHintsDismissed() {
        updateSuccessState { it.copy(showWatchAdForHintsDialog = false) }
    }

    private fun useHint() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is LevelUiState.Success && currentState.remainingHints > 0) {
                userPreferencesRepository.setRemainingHints(currentState.remainingHints - 1)

                val colorToHint = getHintUseCase(
                    board = currentState.board,
                    currentColorBeingCleared = currentState.currentColorBeingCleared
                )

                if (colorToHint != null) {
                    _uiState.value = currentState.copy(
                        hintedColor = colorToHint,
                        remainingHints = currentState.remainingHints - 1
                    )

                    // Remove hint after delay
                    kotlinx.coroutines.delay(GameRules.HINT_DELAY_MS)
                    updateSuccessState { it.copy(hintedColor = null) }
                }
            }
        }
    }
}
