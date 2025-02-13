package es.thatapps.fullbus.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.navigation.Routes
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

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
            } else {
                // Si el documento no existe, lo creamos con el campo "PFP"
                val userData = hashMapOf("PFP" to newPFP)
                firestoreRef.set(userData)
            }
        }
    }

    suspend fun getUserName(): String {
       return authRepository.getUserName()
    }

    fun updateUserName(newUsername: String) {
        viewModelScope.launch {
            try {
                val email = FirebaseAuth.getInstance().currentUser?.email
                if (email != null) {
                    authRepository.updateUserName(email, newUsername)
                }
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error al actualizar el username: ${e.message}", e)
            }
        }
    }

    fun deleteAccount(navController: NavController, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.deleteAccount()
            result.fold(
                onSuccess = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onFailure = { e ->
                    onFailure(e)
                }
            )
        }
    }

    fun logout(navController: NavController) {
        authRepository.logout()
        navController.navigate("login") {
            popUpTo(Routes.Home.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }
}