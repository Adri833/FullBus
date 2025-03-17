package es.thatapps.fullbus.navigation

sealed class Routes(val route: String) {

    data object Login: Routes("login")
    data object Register: Routes("register")
    data object Home: Routes("home")
    data object Settings: Routes("settings")
    data object BusDetail : Routes("busLineDetail/{busLineId}")
    data object Profile : Routes("profile")
}