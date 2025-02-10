package es.thatapps.fullbus.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.utils.AsyncResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _registerState = MutableStateFlow<AsyncResult<Unit>>(AsyncResult.Idle)
    val registerState: StateFlow<AsyncResult<Unit>> = _registerState

    fun register(email: String, password: String) {
        // Validación de campos
        val validationResult = validateFields(email, password)
        if (validationResult != null) {
            _registerState.value = validationResult
            return
        }

        _registerState.value = AsyncResult.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                authRepository.registerUserInFirestore(email)
                _registerState.value = AsyncResult.Success(Unit)
            } else {
                // Obtener la excepción para identificar el tipo de error
                val exception = result.exceptionOrNull()
                val errorResId = when (exception) {
                    is FirebaseAuthUserCollisionException -> R.string.email_in_use // Error de correo ya registrado
                    else -> R.string.unknown_error // Otros errores desconocidos
                }
                _registerState.value = AsyncResult.Error(errorResId)
            }
        }
    }

    // Función para validar los campos de entrada
    private fun validateFields(email: String, password: String): AsyncResult.Error? {
        return when {
            email.isEmpty() || password.isEmpty() -> AsyncResult.Error(R.string.camp_required)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> AsyncResult.Error(R.string.invalid_email)
            password.length < 6 -> AsyncResult.Error(R.string.password_short)
            else -> null
        }
    }

    // Funcion para resetear el estado del registro
    fun resetRegisterState() {
        _registerState.value = AsyncResult.Idle
    }
}