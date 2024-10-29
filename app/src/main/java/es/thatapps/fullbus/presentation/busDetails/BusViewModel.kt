package es.thatapps.fullbus.presentation.busDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.preferences.BusPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val busPreferences: BusPreferences
) : ViewModel() {

    // Recibe el ID de la linea de autobus
    fun getBusFullState(busLineId: String): StateFlow<Boolean> {
        return busPreferences.isBusFull(busLineId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }

    // Metodo para reportar que un autobus esta lleno
    fun reportBusFull(busLineId: String) {
        viewModelScope.launch {
            busPreferences.setBusFull(busLineId, true)
        }
    }

    fun setBusAvailable(busLineId: String) {
        viewModelScope.launch {
            busPreferences.setBusFull(busLineId, false)
        }
    }
}