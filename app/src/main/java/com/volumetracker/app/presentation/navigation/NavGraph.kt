package com.volumetracker.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.volumetracker.app.presentation.dashboard.DashboardScreen
import com.volumetracker.app.presentation.detail.CoinDetailScreen
import com.volumetracker.app.presentation.search.SearchScreen
import com.volumetracker.app.presentation.settings.SettingsScreen
import com.volumetracker.app.presentation.watchlist.WatchlistScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToDetail = { address ->
                    navController.navigate(Screen.CoinDetail.createRoute(address))
                }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateToDetail = { address ->
                    navController.navigate(Screen.CoinDetail.createRoute(address))
                }
            )
        }
        
        composable(Screen.Watchlist.route) {
            WatchlistScreen(
                onNavigateToDetail = { address ->
                    navController.navigate(Screen.CoinDetail.createRoute(address))
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        
        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString("address") ?: return@composable
            CoinDetailScreen(
                tokenAddress = address,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
