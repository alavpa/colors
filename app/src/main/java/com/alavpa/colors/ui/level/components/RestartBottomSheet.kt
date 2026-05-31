package com.alavpa.colors.ui.level.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alavpa.colors.R
import com.alavpa.colors.ui.theme.ColorsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestartBottomSheet(
    onRestartFromLevel1: () -> Unit,
    onResetBoard: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        RestartBottomSheetContent(
            onRestartFromLevel1 = onRestartFromLevel1,
            onResetBoard = onResetBoard,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun RestartBottomSheetContent(
    onRestartFromLevel1: () -> Unit,
    onResetBoard: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.restart_options_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(stringResource(R.string.restart_options_message))
        Spacer(modifier = Modifier.padding(16.dp))
        
        Button(
            onClick = {
                onResetBoard()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.reset_current_board))
        }

        Spacer(modifier = Modifier.padding(8.dp))
        
        Button(
            onClick = {
                onRestartFromLevel1()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.restart_from_level_1))
        }

        Spacer(modifier = Modifier.padding(8.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.close))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RestartBottomSheetPreview() {
    ColorsTheme {
        RestartBottomSheetContent(
            onRestartFromLevel1 = {},
            onResetBoard = {},
            onDismiss = {}
        )
    }
}
