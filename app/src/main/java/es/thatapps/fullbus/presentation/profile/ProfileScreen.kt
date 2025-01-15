package es.thatapps.fullbus.presentation.profile

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header

@Composable
fun ProfileScreen(
    navigationToSettings: () -> Unit,
    navigationToRegister: () -> Unit,
    navigationToProfile: () -> Unit
) {
    // Estados del menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerMenu = DrawerMenu()

    // Estructura principal con el menú lateral y el header
    drawerMenu.Show(
        drawerState = drawerState,
        navigationToRegister = navigationToRegister,
        navigationToSettings = navigationToSettings,
        navigationToProfile = navigationToProfile,
    ) {
        Header(onMenuClick = { drawerMenu.openMenu(scope, drawerState) }, onProfileClick = {})
    }
}