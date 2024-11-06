package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.remote.BusRepository
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val busRepository: BusRepository
) : ViewModel() {

    // Recoge la hora de madrid
    private val madridTimeZone = TimeZone.getTimeZone("Europe/Madrid")

    // Estado para almacenar los buses activos
    private val _activeBuses = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val activeBuses: StateFlow<List<BusDetailDomain>> = _activeBuses

    init {
        checkActiveBuses() // Verifica y carga los buses al iniciar
    }

    private fun checkActiveBuses() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val busesFromFirestore = busRepository.getActiveBuses()

            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

            // Recorre los buses actuales y los compara con los ya creados en Firestore
            val busesToAdd = busesFromFirestore.filter { bus ->
                val departureDate = sdf.parse(bus.departureTime)
                val arriveDate = sdf.parse(bus.arriveTime)
                val currentDate = Date(currentTime)

                // Compara la hora actual con la hora de salida y la hora de llegada
                currentDate.after(departureDate) && currentDate.before(arriveDate)
            }

            // Agrega los buses si no están en la base de datos
            busesToAdd.forEach { bus ->
                if (!busesFromFirestore.contains(bus)) {
                    busRepository.addBus(bus)
                }
            }

            // Actualiza el estado de los buses activos
            _activeBuses.value = busRepository.getActiveBuses()
        }
    }

    // Funcion para poenr el bus como lleno
    fun reportFull(busLineId: String) {
        viewModelScope.launch {
            val bus = _activeBuses.value.find { it.line == busLineId }
            if (bus != null) {
                // Marca el bus como lleno
                bus.isFull = true
                busRepository.updateBus(bus) // Se actualiza el estado de bus en firestore
                checkActiveBuses() // Recarga los buses activos
            }
        }
    }

    // Funcion para poner el bus como disponible
    fun setBusAvailable(busLine: String) {
        viewModelScope.launch {
            val bus = _activeBuses.value.find { it.line == busLine }
            if (bus != null) {
                // Marcamos el bus como disponible
                bus.isFull = false
                busRepository.updateBus(bus)  // Se actualiza el estado del bus en Firestore
                checkActiveBuses()  // Recargamos los buses activos
            }
        }
    }
}