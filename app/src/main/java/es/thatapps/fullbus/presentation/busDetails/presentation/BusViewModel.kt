package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.local.busSchedule.BusScheduleRepository
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor() : ViewModel() {
    private val _busLines = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val busLines: StateFlow<List<BusDetailDomain>> = _busLines

    // Maneja la hora en formato "HH::mm"
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    // Recoge la hora de madrid
    private val madridTimeZone = TimeZone.getTimeZone("Europe/Madrid")

    // Método para inicializar los detalles de una línea específica de autobús
    fun initBusDetails(busLineId: String) {
        viewModelScope.launch {
            checkBusDepartures(busLineId)
        }
    }

    // Método para verificar las salidas de autobuses
    private fun checkBusDepartures(busLineId: String) {
        // Obtiene la hora actual
        val currentTime = Calendar.getInstance(madridTimeZone)
        val currentHour = timeFormatter.format(currentTime.time)

        // Filtra los autobuses segun la linea y la hora actual
        val activeBuses = BusScheduleRepository.busSchedules
            .filter { it.line == busLineId}
            .flatMap { schedule ->
                // Selecciona el horario segun sea sabado, festivo o normal
                val lineschedule = when {
                    checkIfHoliday(currentTime) -> schedule.holidaySchedule
                    currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY -> schedule.saturdaySchedule
                    else -> schedule.normalSchedule
                }

                // Crea la hora de salida como una instancia de Calendar
                lineschedule.filter { it == currentHour }.map { scheduleTime ->
                    val departureTime = Calendar.getInstance(madridTimeZone).apply {
                        time = timeFormatter.parse(scheduleTime)!! // Parsea la hora al formato Calendar
                    }

                    // Crea una instancia con los detalles del bus
                    val busDetail = BusDetailDomain(
                        line = schedule.line,
                        time = scheduleTime,
                        isFull = false,
                        departureTime = departureTime
                    )

                    // Inicia una corrutina para eliminar el bus despues de 1h
                    viewModelScope.launch {
                        kotlinx.coroutines.delay(3600000L) // Espera 1h
                        removeBus(busDetail)
                    }

                    busDetail // Devuelve la instancia creada
                }
            }

        // Actualiza la lista cuando hay un bus activo
        if (activeBuses.isNotEmpty()) {
            _busLines.update { it + activeBuses }
        }
    }

    // Metodo para eliminar un autobus especifico despues de 1h
    private fun removeBus(busDetail: BusDetailDomain) {
        _busLines.update { buses -> buses.filterNot { it == busDetail } }
    }

    // Marcar un autobús como lleno
    fun reportBusFull(busLineId: String) {
        updateBusStatus(busLineId, true)
    }

    // Restablecer un autobús a disponible
    fun setBusAvailable(busLineId: String) {
        updateBusStatus(busLineId, false)
    }

    // Actualizar el estado del autobús
    private fun updateBusStatus(busLineId: String, isFull: Boolean) {
        viewModelScope.launch {
            _busLines.update { buses ->
                buses.map { busDetail ->
                    if (busDetail.line == busLineId) {
                        busDetail.copy(isFull = isFull)
                    } else {
                        busDetail
                    }
                }
            }
        }
    }

    // Método para introducir los dias festivos
    private fun checkIfHoliday(currentTime: Calendar): Boolean {
        // TODO: Agregar mas dias festivos
        return currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }
}