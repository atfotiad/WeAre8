package com.atfotiad.weare8.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.atfotiad.weare8.FeedViewModel
import com.atfotiad.weare8.ui.FeedListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: FeedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = FeedListScreen.route

    ) {
        composable(route = FeedListScreen.route) {
            FeedListScreen(viewModel)
        }
    }
}