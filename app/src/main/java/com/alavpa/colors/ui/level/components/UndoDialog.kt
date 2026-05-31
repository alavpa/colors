package com.alavpa.colors.ui.level.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alavpa.colors.ui.theme.ColorsTheme

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

@Preview(showSystemUi = true)
@Composable
fun UndoDialogPreview() {
    ColorsTheme {
        UndoDialog(onUndo = {}, onCancel = {})
    }
}
