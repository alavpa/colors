package com.alavpa.colors.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alavpa.colors.domain.infrastructure.AdManager
import com.alavpa.colors.domain.infrastructure.AnalyticsManager
import com.alavpa.colors.domain.infrastructure.SoundManager
import com.alavpa.colors.ui.level.LevelScreen
import com.alavpa.colors.ui.level.LevelViewModel
import com.alavpa.colors.ui.navigation.LevelRoute
import com.alavpa.colors.ui.theme.ColorsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var soundManager: SoundManager

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        analyticsManager.trackAppOpen()
        enableEdgeToEdge()
        adManager.loadInterstitial(this)
        adManager.loadRewarded(this)
        setContent {
            ColorsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = LevelRoute()) {
                    composable<LevelRoute> { backStackEntry ->
                        val route: LevelRoute = backStackEntry.toRoute()
                        val viewModel: LevelViewModel = hiltViewModel()

                        LevelScreen(
                            levelId = route.levelId,
                            viewModel = viewModel,
                            adManager = adManager,
                            soundManager = soundManager,
                            onNavigateToLevel = { levelId ->
                                navController.navigate(LevelRoute(levelId)) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
