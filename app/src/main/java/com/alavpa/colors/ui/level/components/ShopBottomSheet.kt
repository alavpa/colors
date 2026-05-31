package com.alavpa.colors.ui.level.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import com.alavpa.colors.ui.theme.ColorsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopBottomSheet(
    onDismiss: () -> Unit,
    onBuyRemoveAds: () -> Unit,
    onBuyHints: () -> Unit,
    onWatchAdForHints: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        ShopBottomSheetContent(
            onBuyRemoveAds = onBuyRemoveAds,
            onBuyHints = onBuyHints,
            onWatchAdForHints = onWatchAdForHints,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun ShopBottomSheetContent(
    onBuyRemoveAds: () -> Unit,
    onBuyHints: () -> Unit,
    onWatchAdForHints: () -> Unit,
    onDismiss: () -> Unit
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
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            onClick = {
                onWatchAdForHints()
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Watch Ad for 3 Hints (FREE)")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ShopBottomSheetPreview() {
    ColorsTheme {
        ShopBottomSheetContent(
            onDismiss = {},
            onBuyRemoveAds = {},
            onBuyHints = {},
            onWatchAdForHints = {}
        )
    }
}
