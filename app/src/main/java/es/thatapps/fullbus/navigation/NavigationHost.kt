package es.thatapps.fullbus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.thatapps.fullbus.presentation.busDetails.presentation.BusDetailScreen
import es.thatapps.fullbus.presentation.home.presentation.HomeScreen
import es.thatapps.fullbus.presentation.login.LoginScreen
import es.thatapps.fullbus.presentation.profile.ProfileScreen
import es.thatapps.fullbus.presentation.register.RegisterScreen


@Composable
fun NavigationHost(

) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
    ) {
        // Navegacion de la pantalla Register
        composable(route = Routes.Register.route) {
            RegisterScreen(
                navigationToLogin = {
                    navController.navigate(Routes.Login.route)
                },
                navigationToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
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
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Navegacion de la pantalla Home
        composable(route = Routes.Home.route) {
            HomeScreen(
                onBusLineClick = { busLine ->
                    // Navegar a BusDetailScreen con el busLine
                    navController.navigate("busLineDetail/$busLine")
                },
                navigationToLogin = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                },
                navigationToProfile = {
                    navController.navigate(Routes.Profile.route)
                },
                navigationToHome = {
                    navController.navigate(Routes.Home.route)
                }
            )
        }

        // Navegación de la pantalla de detalles de la línea de autobús
        composable(route = "busLineDetail/{busLine}") { backStackEntry ->
            val busLine = backStackEntry.arguments?.getString("busLine")
            if (busLine != null) {
                BusDetailScreen(
                    navController = navController,
                    busLine = busLine, // Pasar el busLine a la pantalla de detalles
                    navigationToLogin = {
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Home.route) { inclusive = true }
                        }
                    },
                    navigationToHome = { navController.navigate(Routes.Home.route) },
                    navigationToProfile = { navController.navigate(Routes.Profile.route) },
                )
            }
        }

        // Navegación de la pantalla de perfil
        composable(route = Routes.Profile.route) {
            ProfileScreen(
                navController = navController
            )
        }
    }
}