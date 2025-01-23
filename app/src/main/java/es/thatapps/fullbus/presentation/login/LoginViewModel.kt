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
    private val auth: FirebaseAuth, // Inyección de Firebase Authenticator
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<AsyncResult<Unit>>(AsyncResult.Idle)
    val authState: StateFlow<AsyncResult<Unit>> = _authState


    // Funcion para iniciar sesion
    fun login(email: String, password: String) {
        // Excepciones iniciales
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AsyncResult.Error(R.string.camp_required)
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AsyncResult.Error(R.string.invalid_email)
            return
        }

        _authState.value = AsyncResult.Loading

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await() // Iniciar Sesion
                _authState.value = AsyncResult.Success(Unit)
            } catch (e: Exception) {
                // Manejo de excepciones personalizados
                val errorMessageID = when (e) {
                    is FirebaseAuthInvalidUserException -> R.string.user_not_found // Usuario no registrado
                    is FirebaseAuthInvalidCredentialsException -> R.string.invalid_credentials // Contraseña incorrecta
                    else -> R.string.error_login // Error generico
                }
                _authState.value = AsyncResult.Error(errorMessageID)
            }
        }
    }

    // Funcion para reestablecer la contraseña
    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authState.value =
                AsyncResult.Error(R.string.camp_required) // Mensaje de error si el campo está vacío
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value =
                AsyncResult.Error(R.string.invalid_email) // Mensaje de error si el email no es válido
            return
        }

        _authState.value = AsyncResult.Loading

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _authState.value =
                    AsyncResult.Success(Unit) // Estado personalizado para indicar que se ha enviado el correo de restablecimiento
            } catch (e: Exception) {
                _authState.value =
                    AsyncResult.Error(R.string.error_reset_password) // Mensaje de error si no se puede enviar el correo
            }
        }
    }

    // Funcion para resetear el estado del login
    fun resetAsyncResult() {
        _authState.value = AsyncResult.Idle
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
            } ?: (Exception("No se obtuvo el token de ID de Google"))

        } catch (e: Exception) {
            _authState.value = AsyncResult.Error("Error al iniciar sesion con Google")
        }
    }
}