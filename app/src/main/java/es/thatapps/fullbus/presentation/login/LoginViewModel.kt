package es.thatapps.fullbus.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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

    fun login(email: String, password: String) {
        // Control de excepcion
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
                _loginState.value = LoginState.Error(R.string.error_login)
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
    data class Error(val messageResID: Int) : LoginState()
}