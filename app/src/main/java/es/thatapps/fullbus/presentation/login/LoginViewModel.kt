package es.thatapps.fullbus.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth, // Inyección de Firebase Authenticator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    // Funcion para iniciar sesion
    fun login(email: String, password: String) {
        // Excepciones iniciales
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error(R.string.camp_required)
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error(R.string.invalid_email)
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await() // Iniciar Sesion
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                // Manejo de excepciones personalizados
                val errorMessageID = when (e) {
                    is FirebaseAuthInvalidUserException -> R.string.user_not_found // Usuario no registrado
                    is FirebaseAuthInvalidCredentialsException -> R.string.invalid_credentials // Contraseña incorrecta
                    else -> R.string.error_login // Error generico
                }
                _loginState.value = LoginState.Error(errorMessageID)
            }
        }
    }

    // Funcion para reestablecer la contraseña
    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _loginState.value =
                LoginState.Error(R.string.camp_required) // Mensaje de error si el campo está vacío
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value =
                LoginState.Error(R.string.invalid_email) // Mensaje de error si el email no es válido
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _loginState.value =
                    LoginState.PasswordResetSuccess // Estado personalizado para indicar que se ha enviado el correo de restablecimiento
            } catch (e: Exception) {
                _loginState.value =
                    LoginState.Error(R.string.error_reset_password) // Mensaje de error si no se puede enviar el correo
            }
        }
    }

    // Funcion para resetear el estado del login
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data object PasswordResetSuccess : LoginState() // Metodo para indicar que el correo de restablecimiento se ha enviado
    data class Error(val messageResID: Int) : LoginState()
}