package com.atfotiad.weare8

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.atfotiad.weare8.navigation.AppNavHost
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeAre8Application : Application() {

    companion object {
        @Composable
        fun FeedApp() {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                val viewModel: FeedViewModel = hiltViewModel()
                AppNavHost(
                    navController = navController, viewModel
                )
            }
        }
    }
}