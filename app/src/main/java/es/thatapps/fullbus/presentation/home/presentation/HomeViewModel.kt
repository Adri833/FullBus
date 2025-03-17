package es.thatapps.fullbus.presentation.home.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun logout() {
        authRepository.logout()
    }
}