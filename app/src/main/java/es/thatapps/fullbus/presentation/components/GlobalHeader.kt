package es.thatapps.fullbus.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun GlobalHeader(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // Observamos el estado de la imagen base64 del perfil
    val userBase64Image by viewModel.userBase64Image.collectAsState()

    // Si aún no se ha cargado la imagen, la cargamos
    LaunchedEffect(true) {
        val userEmail = "pepito@pepe.es" // TODO recoger correo de auth
        viewModel.loadUserProfileImage(userEmail)
    }

    // Pasamos la imagen base64 al Header
    Header(
        onMenuClick = {},
        userBase64Image = userBase64Image
    )
}
