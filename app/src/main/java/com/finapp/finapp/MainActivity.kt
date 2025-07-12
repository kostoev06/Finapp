package com.finapp.finapp

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.finapp.feature.account.di.LocalFeatureAccountComponentBuilder
import com.finapp.feature.common.theme.FinappTheme
import com.finapp.feature.expenses.di.LocalFeatureExpensesComponentBuilder
import com.finapp.feature.home.HomeScreen
import com.finapp.feature.home.di.LocalFeatureHomeComponentBuilder
import com.finapp.feature.income.di.LocalFeatureIncomeComponentBuilder
import com.finapp.feature.tags.di.LocalFeatureTagsComponentBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)
        initSplashScreen()
        enableEdgeToEdge()

        val appComponent = (application as FinappApplication).appComponent

        setContent {
            FinappTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalFeatureHomeComponentBuilder provides appComponent.featureHomeComponentBuilder(),
                    LocalFeatureAccountComponentBuilder provides appComponent.featureAccountComponentBuilder(),
                    LocalFeatureIncomeComponentBuilder provides appComponent.featureIncomeComponentBuilder(),
                    LocalFeatureExpensesComponentBuilder provides appComponent.featureExpensesComponentBuilder(),
                    LocalFeatureTagsComponentBuilder provides appComponent.featureTagsComponentBuilder()
                )  {
                    HomeScreen(
                        navController = navController,
                        modifier = Modifier.safeDrawingPadding()
                    )
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
