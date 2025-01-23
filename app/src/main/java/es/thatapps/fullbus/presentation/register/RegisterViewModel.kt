package es.thatapps.fullbus.presentation.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.utils.encodeImageToBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, context: Context) {
        // Validación de campos
        val validationResult = validateFields(email, password)
        if (validationResult != null) {
            _registerState.value = validationResult
            return
        }

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                // Guardar el nombre de usuario en Firestore
                val username = authRepository.generateRandomUsername() // Genera un nombre aleatorio
                saveUserToFirestore(email, username, context)
                _registerState.value = RegisterState.Success
            } else {
                // Obtener la excepción para identificar el tipo de error
                val exception = result.exceptionOrNull()
                val errorResId = when (exception) {
                    is FirebaseAuthUserCollisionException -> R.string.email_in_use // Error de correo ya registrado
                    else -> R.string.error_unknown // Otros errores desconocidos
                }
                _registerState.value = RegisterState.Error(errorResId)
            }
        }
    }

    // Función para validar los campos de entrada
    private fun validateFields(email: String, password: String): RegisterState.Error? {
        return when {
            email.isEmpty() || password.isEmpty() -> RegisterState.Error(R.string.camp_required)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> RegisterState.Error(R.string.invalid_email)
            password.length < 6 -> RegisterState.Error(R.string.password_short)
            else -> null
        }
    }

    // Funcion para resetear el estado del registro
    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    // Guardar el usuario en Firestore si el email no está registrado
    private fun saveUserToFirestore(email: String, username: String, context: Context) {

        // Foto de perfil predeterminada
        val defaultPFP = authRepository.getDefaultPFP(context)

        val userData = hashMapOf(
            "username" to username,
            "email" to email,
            "PFP" to defaultPFP
        )

        // Si no está registrado, proceder a guardar los datos del usuario
        firestore.collection("users").document(email).set(userData)
            .addOnSuccessListener {
            _registerState.value = RegisterState.Success
        }
            .addOnFailureListener {
            // Manejo de errores genéricos al intentar guardar en Firestore
            _registerState.value = RegisterState.Error(R.string.error_firestore)
        }
    }
}

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data class Error(val messageResID: Int) : RegisterState()
}