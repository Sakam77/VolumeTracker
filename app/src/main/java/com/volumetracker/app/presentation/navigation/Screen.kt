package com.volumetracker.app.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Search : Screen("search")
    object Watchlist : Screen("watchlist")
    object Settings : Screen("settings")
    object CoinDetail : Screen("coin_detail/{address}") {
        fun createRoute(address: String) = "coin_detail/$address"
    }
    object Disclaimer : Screen("disclaimer")
}
