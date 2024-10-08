package es.thatapps.fullbus.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    //TODO Aqui va toda la lógica de la pantalla de registro

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            _registerState.value = if (result.isSuccess) {
                RegisterState.Success
            } else {
                RegisterState.Error(result.exceptionOrNull()?.message ?: "Uknown error")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}