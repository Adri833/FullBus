package es.thatapps.fullbus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.thatapps.fullbus.presentation.busDetails.presentation.BusDetailScreen
import es.thatapps.fullbus.presentation.home.presentation.HomeScreen
import es.thatapps.fullbus.presentation.login.LoginScreen
import es.thatapps.fullbus.presentation.register.RegisterScreen
import es.thatapps.fullbus.presentation.settings.SettingsScreen


@Composable
fun NavigationHost(

) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
    ) {
        // Navegacion de la pantalla Register
        composable(route = Routes.Register.route) {
            RegisterScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route)
                },
                navigationToHome = {
                    navController.navigate(Routes.Home.route)
                }
            )
        }

        // Navegacion de la pantalla Login
        composable(route = Routes.Login.route) {
            LoginScreen(
                navigationToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                navigationToHome = {
                    navController.navigate(Routes.Home.route)
                }
            )
        }

        // Navegacion de la pantalla Home
        composable(route = Routes.Home.route) {
            HomeScreen(
                onBusLineClick = { busLineId ->
                    navController.navigate("busLineDetail/$busLineId")
                },
                navigationToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                navigationToSettings = {
                    navController.navigate(Routes.Settings.route)
                }
            )
        }

        // Navegacion de la pantalla Settings
        composable(route = Routes.Settings.route) {
            SettingsScreen()
        }

        // Navegación de la pantalla de detalles de la línea de autobús
        composable(
            route = Routes.BusDetail.route,
            arguments = listOf(navArgument("busLineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val busLineId = backStackEntry.arguments?.getString("busLineId") ?: return@composable
            BusDetailScreen(
                busLineId = busLineId,
                onBack = { navController.popBackStack() },
                navigationToRegister = { navController.navigate(Routes.Register.route) },
                navigationToSettings = { navController.navigate(Routes.Settings.route) }
            )
        }


    }
}