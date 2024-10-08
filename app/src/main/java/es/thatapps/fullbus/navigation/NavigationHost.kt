package es.thatapps.fullbus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.thatapps.fullbus.presentation.login.LoginScreen
import es.thatapps.fullbus.presentation.register.RegisterScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Register.route,
    ) {
        composable(route = Routes.Register.route) {
//           RegisterScreen(navController = navController) esto es una colada como una catedral
            RegisterScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route)
                }
            )
        }

        composable(route = Routes.Login.route) {
            LoginScreen()
        }
    }
}