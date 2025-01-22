package es.thatapps.fullbus.presentation.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.thatapps.fullbus.presentation.components.DrawerMenu
import es.thatapps.fullbus.presentation.components.Header
import es.thatapps.fullbus.utils.ImageBase64
import es.thatapps.fullbus.utils.encodeImageToBase64
import es.thatapps.fullbus.utils.imagePickerLauncher
import es.thatapps.fullbus.utils.getPFP
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navigationToSettings: () -> Unit,
    navigationToRegister: () -> Unit,
    navigationToProfile: () -> Unit,
    navigationToHome: () -> Unit
) {
    val context = LocalContext.current // Obtén el contexto aquí
    // Estados del menú
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerMenu = DrawerMenu()

    // Lector de imágenes (Selector de imágenes)
    val imagePicker = imagePickerLauncher { uri: Uri? ->
        uri?.let {
            // Convertimos la imagen seleccionada a Base64
            val base64Image = encodeImageToBase64(context, it)
            Log.d("ProfileScreen", "Imagen convertida a Base64: $base64Image") // Verifica que el Base64 no sea null

            base64Image?.let { encodedImage ->
                // Subimos la imagen codificada a Firestore
                updatePFP(encodedImage)
            } ?: run {
                Toast.makeText(context, "Error al convertir la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Estructura principal con el menú lateral y el header
    drawerMenu.Show(
        drawerState = drawerState,
        navigationToRegister = navigationToRegister,
        navigationToSettings = navigationToSettings,
        navigationToProfile = navigationToProfile,
        navigationToHome = navigationToHome
    ) {
        // Header
        Header(onMenuClick = { scope.launch { drawerState.open() } })

        // Mostrar la imagen de perfil actual
        var pfp by remember { mutableStateOf<String?>(null) }

        // Lanzamos una corutina para obtener el PFP actual de Firestore
        LaunchedEffect(Unit) {
            pfp = getPFP() // Llamada suspendida dentro de una corutina
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen de perfil actual
            pfp?.let { imageUrl ->
                ImageBase64(imageUrl = imageUrl, width = 100.dp, height = 100.dp)
            } ?: run {
                Text(text = "No tienes foto de perfil")
            }

            // Botón para seleccionar y cambiar la imagen de perfil
            Button(
                onClick = {
                    imagePicker.launch("image/*") // Abrimos el selector de imágenes
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Seleccionar foto de perfil")
            }
        }
    }
}

// Función para actualizar el PFP en Firestore
fun updatePFP(newPFP: String) {
    // Obtener el correo electrónico del usuario autenticado
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    if (userEmail == null) {
        Log.e("ProfileScreen", "Usuario no autenticado o sin correo electrónico")
        return
    }

    // Referencia al documento de usuario en Firestore, usando el correo electrónico como identificador
    val firestoreRef = FirebaseFirestore.getInstance()
        .collection("users")
        .document(userEmail) // Utilizamos el correo electrónico como identificador

    // Verificar si el documento ya existe
    firestoreRef.get().addOnSuccessListener { document ->
        if (document.exists()) {
            // Si el documento existe, actualizamos el campo "PFP"
            firestoreRef.update("PFP", newPFP)
                .addOnSuccessListener {
                    Log.d("ProfileScreen", "PFP actualizado exitosamente en Firestore")
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfileScreen", "Error al actualizar PFP: ${exception.message}")
                }
        } else {
            // Si el documento no existe, lo creamos con el campo "PFP"
            val userData = hashMapOf("PFP" to newPFP)
            firestoreRef.set(userData)
                .addOnSuccessListener {
                    Log.d("ProfileScreen", "Nuevo documento de usuario creado con PFP")
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfileScreen", "Error al crear documento de usuario: ${exception.message}")
                }
        }
    }.addOnFailureListener { exception ->
        Log.e("ProfileScreen", "Error al obtener documento de usuario: ${exception.message}")
    }
}