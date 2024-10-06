package es.thatapps.fullbus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.thatapps.fullbus.presentation.register.RegisterScreen

@Composable
fun NavigationHost() {

    NavHost(
        navController = rememberNavController(),
        startDestination = Routes.Register.route,
    ) {
        composable(route = Routes.Register.route) {
           RegisterScreen()
        }
    }

}