package com.alavpa.colors.ui.level

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alavpa.colors.domain.model.RgbColor

import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import com.alavpa.colors.ui.ads.AdManager
import com.alavpa.colors.ui.components.BannerAd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    viewModel: LevelViewModel,
    adManager: AdManager,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showShopDialog by remember { mutableStateOf(false) }

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
            }
        }
    }

    if (showShopDialog) {
        ShopDialog(
            onDismiss = { showShopDialog = false },
            onBuyRemoveAds = { viewModel.buyRemoveAds() },
            onBuyHints = { viewModel.buyHints(10) },
            onRestart = { viewModel.restartGame() }
        )
    }

    if (uiState is LevelUiState.Success && (uiState as LevelUiState.Success).showUndoDialog) {
        UndoDialog(
            onUndo = { viewModel.onUndoAdWatched() },
            onCancel = { viewModel.onUndoCancelled() }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    val state = uiState
                    if (state is LevelUiState.Success) {
                        Text("Level ${state.board.level.id}")
                    }
                },
                actions = {
                    val state = uiState
                    if (state is LevelUiState.Success) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { viewModel.useHint() }
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = "Use Hint")
                            Text(
                                text = state.remainingHints.toString(),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    IconButton(onClick = { showShopDialog = true }) {
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
                        onCellClick = { row, col -> viewModel.onCellClick(row, col) }
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

@Composable
fun ShopDialog(
    onDismiss: () -> Unit,
    onBuyRemoveAds: () -> Unit,
    onBuyHints: () -> Unit,
    onRestart: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing to prevent dismissal on tap outside */ },
        title = { Text("Settings & Shop") },
        text = {
            Column {
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
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    onClick = {
                        onBuyHints()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("10 Hints - $0.99")
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Text("Danger Zone", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    onClick = {
                        onRestart()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Restart from Level 1")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
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
fun LevelContent(
    grid: List<List<RgbColor?>>,
    verticalSize: Int,
    horizontalSize: Int,
    hintedColor: RgbColor?,
    onCellClick: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .aspectRatio(horizontalSize.toFloat() / verticalSize.toFloat())
    ) {
        grid.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.weight(1f)
            ) {
                row.forEachIndexed { colIndex, rgbColor ->
                    Cell(
                        rgbColor = rgbColor,
                        isHinted = rgbColor != null && rgbColor == hintedColor,
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
    isHinted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val color = rgbColor?.toComposeColor() ?: Color.Transparent

    Box(
        modifier = modifier
            .padding(2.dp)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .aspectRatio(1f)
            .then(
                if (isHinted) Modifier.border(4.dp, Color.White, MaterialTheme.shapes.small)
                else Modifier
            )
            .background(color)
            .clickable(enabled = rgbColor != null) { onClick() }
    )
}

fun RgbColor.toComposeColor(): Color {
    return Color(red, green, blue, alpha)
}
