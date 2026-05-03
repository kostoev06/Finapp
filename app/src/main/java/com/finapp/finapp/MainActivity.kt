package com.finapp.finapp

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.ThemeMode
import com.finapp.core.settings.api.model.ThemeSettings
import com.finapp.core.work.transaction.SyncTransactionWorker
import com.finapp.feature.account.di.LocalFeatureAccountComponentBuilder
import com.finapp.feature.common.sound.LocalSoundPlayer
import com.finapp.feature.common.theme.FinappTheme
import com.finapp.feature.expenses.di.LocalFeatureExpensesComponentBuilder
import com.finapp.feature.home.HomeScreen
import com.finapp.feature.home.di.LocalFeatureHomeComponentBuilder
import com.finapp.feature.income.di.LocalFeatureIncomeComponentBuilder
import com.finapp.feature.settings.di.LocalFeatureSettingsComponentBuilder
import com.finapp.feature.settings.passcode.PasscodeRoute
import com.finapp.feature.tags.di.LocalFeatureTagsComponentBuilder
import com.finapp.finapp.lock.LockState
import com.finapp.finapp.lock.LockViewModel
import com.finapp.finapp.theme.AppThemeViewModel
import com.finapp.finapp.theme.di.LocalFeatureMainComponentBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)
        initSplashScreen()
        enableEdgeToEdge()

        val workRequest = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
            .build()

        WorkManager
            .getInstance(this)
            .enqueue(workRequest)

        val appComponent = (application as FinappApplication).appComponent

        setContent {
            val mainComponent = remember {
                appComponent
                    .featureMainComponentBuilder()
                    .build()
            }

            val appThemeVm: AppThemeViewModel =
                viewModel(factory = mainComponent.viewModelFactory())
            val settings = appThemeVm.themeSettings.collectAsState(
                initial = ThemeSettings(
                    themeMode = ThemeMode.SYSTEM,
                    brandColor = BrandColorOption.GREEN
                )
            ).value
            if (settings != null) {
                FinappTheme(
                    themeMode = settings.themeMode,
                    brand = settings.brandColor
                ) {
                    val navController = rememberNavController()
                    CompositionLocalProvider(
                        LocalFeatureHomeComponentBuilder provides appComponent.featureHomeComponentBuilder(),
                        LocalFeatureAccountComponentBuilder provides appComponent.featureAccountComponentBuilder(),
                        LocalFeatureIncomeComponentBuilder provides appComponent.featureIncomeComponentBuilder(),
                        LocalFeatureExpensesComponentBuilder provides appComponent.featureExpensesComponentBuilder(),
                        LocalFeatureTagsComponentBuilder provides appComponent.featureTagsComponentBuilder(),
                        LocalFeatureSettingsComponentBuilder provides appComponent.featureSettingsComponentBuilder(),
                        LocalFeatureMainComponentBuilder provides appComponent.featureMainComponentBuilder(),
                        LocalSoundPlayer provides appComponent.soundPlayer()
                    ) {
                        val lockVm: LockViewModel =
                            viewModel(factory = mainComponent.viewModelFactory())
                        val lockState by lockVm.state.collectAsState()

                        when (lockState) {
                            LockState.Loading -> Unit
                            LockState.Required -> {
                                val settingsBuilder = LocalFeatureSettingsComponentBuilder.current
                                val settingsComponent = remember { settingsBuilder.build() }
                                PasscodeRoute(
                                    viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
                                    onBack = {},
                                    onDone = lockVm::markUnlocked,
                                    showBack = false,
                                    modifier = Modifier.safeDrawingPadding()
                                )
                            }
                            LockState.NotRequired,
                            LockState.Unlocked -> HomeScreen(
                                navController = navController,
                                modifier = Modifier.safeDrawingPadding()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    1.0f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    1.0f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                zoomX.start()
                zoomY.start()
            }
        }
    }

}
