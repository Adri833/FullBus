package es.thatapps.fullbus.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(email, password)
        }
    }
}