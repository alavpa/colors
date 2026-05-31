package com.alavpa.colors.ui.level

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alavpa.colors.domain.model.RgbColor

import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import com.alavpa.colors.domain.infrastructure.AdManager
import com.alavpa.colors.domain.infrastructure.SoundManager
import com.alavpa.colors.ui.components.BannerAd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    viewModel: LevelViewModel,
    adManager: AdManager,
    soundManager: SoundManager,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showShopDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            val activity = context as? Activity ?: return@collect
            when (event) {
                is LevelUiEvent.ShowInterstitial -> {
                    adManager.showInterstitial(activity, event.onDismissed)
                }
                is LevelUiEvent.ShowRewarded -> {
                    adManager.showRewarded(activity, event.onRewarded, event.onDismissed)
                }
                is LevelUiEvent.ShowHintConfirmation -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Are you sure you want to use a hint? It will cost 1 hint point.",
                        actionLabel = "Confirm",
                        withDismissAction = true
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        event.onConfirmed()
                    }
                }
                is LevelUiEvent.ShowShop -> {
                    showShopDialog = true
                }
            }
        }
    }

    if (showShopDialog) {
        ShopBottomSheet(
            onDismiss = { showShopDialog = false },
            onBuyRemoveAds = { viewModel.buyRemoveAds() },
            onBuyHints = { viewModel.buyHints(10) }
        )
    }

    if (uiState is LevelUiState.Success && (uiState as LevelUiState.Success).showUndoDialog) {
        UndoDialog(
            onUndo = { viewModel.onUndoAdWatched() },
            onCancel = { viewModel.onUndoCancelled() }
        )
    }

    if (uiState is LevelUiState.Success && (uiState as LevelUiState.Success).showRestartOptions) {
        RestartDialog(
            onRestartFromLevel1 = { viewModel.restartGame() },
            onResetBoard = { viewModel.resetBoard() },
            onDismiss = { viewModel.onRestartOptionsDismissed() }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    val state = uiState
                    if (state is LevelUiState.Success) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "LEVEL ${state.board.level.id}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }

                            IconButton(onClick = { viewModel.onRestartClicked() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Restart Options")
                            }
                        }
                    }
                },
                actions = {
                    val state = uiState
                    if (state is LevelUiState.Success) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { 
                                    viewModel.onHintClicked()
                                }
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = "Use Hint")
                            Text(
                                text = state.remainingHints.toString(),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        IconButton(onClick = { viewModel.toggleMute() }) {
                            Icon(
                                imageVector = if (state.isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = if (state.isMuted) "Unmute" else "Mute"
                            )
                        }
                    }
                    IconButton(onClick = { 
                        showShopDialog = true 
                    }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shop")
                    }
                }
            )
        },
        bottomBar = {
            val state = uiState
            if (state is LevelUiState.Success && !state.isAdsRemoved) {
                BannerAd()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is LevelUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is LevelUiState.Success -> {
                    LevelContent(
                        grid = state.board.grid,
                        verticalSize = state.board.level.verticalSize,
                        horizontalSize = state.board.level.horizontalSize,
                        hintedColor = state.hintedColor,
                        onCellClick = { row, col -> 
                            if (!state.isMuted) {
                                soundManager.playTapSound()
                            }
                            viewModel.onCellClick(row, col) 
                        }
                    )
                }

                is LevelUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopBottomSheet(
    onDismiss: () -> Unit,
    onBuyRemoveAds: () -> Unit,
    onBuyHints: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Settings & Shop",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text("Support the developer and improve your game!")
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    onBuyRemoveAds()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove Ads - $0.99")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    onBuyHints()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("10 Hints - $0.99")
            }
        }
    }
}

@Composable
fun UndoDialog(
    onUndo: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing to prevent dismissal on tap outside */ },
        title = { Text("Mistake!") },
        text = { Text("You clicked the wrong color. Watch an ad to keep your progress, or reset the board.") },
        confirmButton = {
            Button(onClick = onUndo) {
                Text("Watch Ad to Undo")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Reset Board")
            }
        }
    )
}

@Composable
fun RestartDialog(
    onRestartFromLevel1: () -> Unit,
    onResetBoard: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restart") },
        text = { Text("What would you like to do?") },
        confirmButton = {
            Button(
                onClick = onRestartFromLevel1,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Restart from Level 1")
            }
        },
        dismissButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onResetBoard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Current Board")
                }
                Spacer(modifier = Modifier.padding(4.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun LevelContent(
    grid: List<List<RgbColor?>>,
    verticalSize: Int,
    horizontalSize: Int,
    hintedColor: RgbColor?,
    onCellClick: (Int, Int) -> Unit
) {
    val anyCellHinted = hintedColor != null

    Column(
        modifier = Modifier
            .padding(16.dp)
            .aspectRatio(
                ratio = horizontalSize.toFloat() / verticalSize.toFloat(),
                matchHeightConstraintsFirst = true
            )
    ) {
        grid.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.weight(1f)
            ) {
                row.forEachIndexed { colIndex, rgbColor ->
                    Cell(
                        rgbColor = rgbColor,
                        isDimmed = anyCellHinted && (rgbColor == null || rgbColor != hintedColor),
                        modifier = Modifier.weight(1f),
                        onClick = { onCellClick(rowIndex, colIndex) }
                    )
                }
            }
        }
    }
}

@Composable
fun Cell(
    rgbColor: RgbColor?,
    isDimmed: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val color = rgbColor?.toComposeColor() ?: Color.Transparent
    val shape = MaterialTheme.shapes.medium

    Box(
        modifier = modifier
            .padding(2.dp)
            .alpha(if (isDimmed) 0.3f else 1.0f)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .aspectRatio(1f)
            .background(color, shape)
            .clip(shape)
            .clickable(enabled = rgbColor != null) { onClick() }
    )
}

fun RgbColor.toComposeColor(): Color {
    return Color(red, green, blue, alpha)
}
