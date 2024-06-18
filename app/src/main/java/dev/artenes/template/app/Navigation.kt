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
    val uri = "https://template.artenes.dev"

    NavHost(navController = navController, startDestination = "main") {

        composable("main") {

            TimerScreen()

        }

        composable("samples", deepLinks = listOf(navDeepLink { uriPattern = "$uri/samples" })) {

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

        composable("fields", deepLinks = listOf(navDeepLink { uriPattern = "$uri/fields" })) {

            FieldsSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            "notifications",
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/notifications" })
        ) {

            NotificationsSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            "services",
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/services" })
        ) {

            ServiceSampleScreen(
                back = {
                    navController.popBackStack()
                }
            )

        }

    }
}
