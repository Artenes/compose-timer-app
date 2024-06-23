package dev.artenes.timer.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import dev.artenes.timer.app.features.timer.TimerScreen

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {

        composable(
            "main",
            deepLinks = listOf(navDeepLink { uriPattern = NavigationConstants.URI })
        ) {

            TimerScreen()

        }

    }

}

object NavigationConstants {

    const val URI = "https://timer.artenes.dev"

}