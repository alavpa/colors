package com.alavpa.colors.ui.level.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alavpa.colors.domain.model.RgbColor
import com.alavpa.colors.ui.theme.GameConstants

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
            .padding(GameConstants.BoardPadding)
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

@Preview(showBackground = true)
@Composable
fun LevelContentPreview() {
    val red = RgbColor(1f, 0f, 0f)
    val blue = RgbColor(0f, 0f, 1f)
    val grid = listOf(
        listOf(red, blue),
        listOf(blue, red)
    )
    LevelContent(
        grid = grid,
        verticalSize = 2,
        horizontalSize = 2,
        hintedColor = null,
        onCellClick = { _, _ -> }
    )
}
