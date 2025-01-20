package es.thatapps.fullbus.presentation.register

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
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
        if (email.isEmpty() || password.isEmpty()) {
            _registerState.value = RegisterState.Error(R.string.camp_required)
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = RegisterState.Error(R.string.invalid_email)
            return
        }

        if (password.length < 6) {
            _registerState.value = RegisterState.Error(R.string.password_short)
            return
        }

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                // Guardar el nombre de usuario en Firestore
                val username = generateRandomUsername() // Genera un nombre aleatorio
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

    // Funcion para generar un nombre aleatorio para el usuario
    private fun generateRandomUsername(): String {
        val randomNumber = (1..9999).random()
        return "User$randomNumber"
    }

    private fun getDefaultPFP(context: Context): String {
        // Redimensionamos la imagen si es necesario
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.borrar)
        val resizedBitmap = resizeBitmap(bitmap, 200) // Redimensionamos a 200px de ancho

        // Convertimos el bitmap redimensionado a Base64
        return bitmapToBase64(resizedBitmap)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Comprimimos la imagen a JPEG con calidad del 80% para optimizar el tamaño
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val newHeight = (maxWidth / ratio).toInt()

        // Redimensionamos la imagen manteniendo la relación de aspecto
        return Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, false)
    }


    // Funcion para resetear el estado del registro
    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    // Guardar el usuario en Firestore si el email no está registrado
    private fun saveUserToFirestore(email: String, username: String, context: Context) {

        // Foto de perfil predeterminada
        val defaultPFP = getDefaultPFP(context)

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