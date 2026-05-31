package com.alavpa.colors.ui.level.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.alavpa.colors.domain.model.RgbColor
import com.alavpa.colors.ui.theme.GameConstants

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
            .padding(GameConstants.CellPadding)
            .alpha(if (isDimmed) GameConstants.DimmedAlpha else 1.0f)
            .sizeIn(minWidth = GameConstants.MinCellSize, minHeight = GameConstants.MinCellSize)
            .aspectRatio(1f)
            .background(color, shape)
            .clip(shape)
            .clickable(enabled = rgbColor != null) { onClick() }
    )
}

fun RgbColor.toComposeColor(): Color {
    return Color(red, green, blue, alpha)
}

@Preview(showBackground = true)
@Composable
fun CellPreview() {
    Cell(
        rgbColor = RgbColor(1f, 0f, 0f),
        isDimmed = false,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CellDimmedPreview() {
    Cell(
        rgbColor = RgbColor(0f, 1f, 0f),
        isDimmed = true,
        onClick = {}
    )
}
