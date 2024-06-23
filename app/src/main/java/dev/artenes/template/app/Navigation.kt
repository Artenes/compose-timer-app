package dev.artenes.template.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import dev.artenes.template.app.features.TimerScreen
import dev.artenes.template.app.samples.fields.FieldsSampleScreen
import dev.artenes.template.app.samples.main.MainSamplesScreen
import dev.artenes.template.app.samples.notifications.NotificationsSampleScreen
import dev.artenes.template.app.samples.services.ServiceSampleScreen

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

        composable(
            "samples",
            deepLinks = listOf(navDeepLink { uriPattern = "${NavigationConstants.URI}/samples" })
        ) {

            MainSamplesScreen(
                navigateToFieldsSample = {
                    navController.navigate("fields")
                },
                navigateToNotificationsSample = {
                    navController.navigate("notifications")
                },
                navigateToServicesSample = {
                    navController.navigate("services")
                }
            )

        }

        composable(
            "fields",
            deepLinks = listOf(navDeepLink { uriPattern = "${NavigationConstants.URI}/fields" })
        ) {

            FieldsSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            "notifications",
            deepLinks = listOf(navDeepLink {
                uriPattern = "${NavigationConstants.URI}/notifications"
            })
        ) {

            NotificationsSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            "services",
            deepLinks = listOf(navDeepLink { uriPattern = "${NavigationConstants.URI}/services" })
        ) {

            ServiceSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

    }

}

object NavigationConstants {

    const val URI = "https://timer.artenes.dev"

}