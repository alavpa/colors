package com.alavpa.colors.ui.level.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alavpa.colors.R
import com.alavpa.colors.ui.theme.ColorsTheme

@Composable
fun UndoDialog(
    onUndo: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing to prevent dismissal on tap outside */ },
        title = { Text(stringResource(R.string.undo_mistake_title)) },
        text = { Text(stringResource(R.string.undo_mistake_message)) },
        confirmButton = {
            Button(onClick = onUndo) {
                Text(stringResource(R.string.watch_ad_to_undo))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.reset_board))
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
