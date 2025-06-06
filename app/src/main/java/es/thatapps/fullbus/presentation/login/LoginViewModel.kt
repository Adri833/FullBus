package es.thatapps.fullbus.presentation.login

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.utils.AsyncResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<AsyncResult<Unit>>(AsyncResult.Idle)
    val authState: StateFlow<AsyncResult<Unit>> = _authState

    private val _passwordResetState = MutableStateFlow<AsyncResult<Unit>>(AsyncResult.Idle)
    val passwordResetState: StateFlow<AsyncResult<Unit>> = _passwordResetState

    // Funcion para iniciar sesion
    fun login(email: String, password: String) {
       if (validateFields(email, password) != null) return
        _authState.value = AsyncResult.Loading

        viewModelScope.launch {
            // Si el email esta registrado, intentar iniciar sesion
            try {
                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    _authState.value = AsyncResult.Success(Unit)
                } else {
                    val errorMessageID = when (result.exceptionOrNull()) {
                        is FirebaseAuthInvalidUserException -> R.string.user_not_found
                        is FirebaseAuthInvalidCredentialsException -> R.string.invalid_credentials
                        else -> R.string.error_login
                    }
                    _authState.value = AsyncResult.Error(errorMessageID)
                }
            } catch (e: Exception) {
                _authState.value = AsyncResult.Error(R.string.error_login)
            }
        }
    }

    // Funcion para reestablecer la contraseña
    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authState.value = AsyncResult.Error("Ingresa tu correo electronico")
            return
        }
        _passwordResetState.value = AsyncResult.Loading

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _passwordResetState.value = AsyncResult.Success(Unit)
            } catch (e: Exception) {
                _passwordResetState.value = AsyncResult.Error(R.string.error_reset_password)
            }
        }
    }

    // Funcion para validar los campos de entrada
    private fun validateFields(email: String, password: String): Any? {
        return when {
            email.isEmpty() || password.isEmpty() -> _authState.value = AsyncResult.Error(R.string.camp_required)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> _authState.value = AsyncResult.Error(R.string.invalid_email)
            password.length < 6 -> _authState.value = AsyncResult.Error(R.string.password_short)
            else -> null
        }
    }

    // Funcion para resetear el estado del login
    fun resetAsyncResult() {
        _authState.value = AsyncResult.Idle
        _passwordResetState.value = AsyncResult.Idle
    }

    fun setLoadingState() {
        _authState.value = AsyncResult.Loading
    }

    // Funcion para saber si el usuario esta logueado
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun googleSignInObserver(result: ActivityResult, oneTapClient: SignInClient) {
        _authState.value = AsyncResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            googleSignIn(result, oneTapClient)
        }
    }

    private suspend fun googleSignIn(result: ActivityResult, oneTapClient: SignInClient) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            credential.googleIdToken?.let { googleIdTokenNotNull ->
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenNotNull, null)
                authRepository.signInWithGoogle(firebaseCredential)
                _authState.value = AsyncResult.Success(Unit)
            } ?: throw Exception("Token de Google nulo")

        } catch (e: Exception) {
            _authState.value = AsyncResult.Error("Error al iniciar sesion con Google")
        }
    }
}