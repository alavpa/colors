package com.alavpa.colors.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alavpa.colors.ui.ads.AdManager
import com.alavpa.colors.ui.level.LevelScreen
import com.alavpa.colors.ui.theme.ColorsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        adManager.loadInterstitial(this)
        adManager.loadRewarded(this)
        setContent {
            ColorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LevelScreen(
                        viewModel = hiltViewModel(),
                        adManager = adManager,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
