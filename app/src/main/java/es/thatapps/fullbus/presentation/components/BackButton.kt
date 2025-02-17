package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import es.thatapps.fullbus.R

@Composable
fun BackButton(navController: NavController) {
    Box {
        IconButton(
            onClick = { navController.popBackStack() } // Vuelve a la pantalla anterior
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back"
            )
        }
    }
}