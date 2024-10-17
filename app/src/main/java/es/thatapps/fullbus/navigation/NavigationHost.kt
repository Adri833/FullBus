package es.thatapps.fullbus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.thatapps.fullbus.presentation.login.LoginScreen
import es.thatapps.fullbus.presentation.register.RegisterScreen
import es.thatapps.fullbus.presentation.main.MainScreen


@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.Main.route,
    ) {
        // Navegacion de la pantalla Register
        composable(route = Routes.Register.route) {
            RegisterScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route)
                },
                navigationToMain = {
                    navController.navigate(Routes.Main.route)
                }
            )
        }

        // Navegacion de la pantalla Login
        composable(route = Routes.Login.route) {
            LoginScreen(
                navigationToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                navigationToMain = {
                    navController.navigate(Routes.Main.route)
                }
            )
        }

        // Navegacion de la pantalla Main
        composable(route = Routes.Main.route) {
            MainScreen()
        }
    }
}