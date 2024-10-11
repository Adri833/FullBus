package es.thatapps.fullbus.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.R
import es.thatapps.fullbus.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore // Inyección de Firestore
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String, username: String) {
        // Control de excepciones
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
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
                saveUserToFirestore(username, email, password)
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error(R.string.error_unknown)
            }
        }
    }

    // Guardar el usuario en Firestore si el email no está registrado
    private fun saveUserToFirestore(username: String, email: String, password: String) {

        // Si no está registrado, proceder a guardar los datos del usuario
        firestore.collection("users").document(email).set(
            hashMapOf("username" to username, "email" to email, "password" to password)
        ).addOnSuccessListener {
            _registerState.value = RegisterState.Success
        }.addOnFailureListener {
            _registerState.value = RegisterState.Error(R.string.error_firestore)
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val messageResID: Int) : RegisterState()
}