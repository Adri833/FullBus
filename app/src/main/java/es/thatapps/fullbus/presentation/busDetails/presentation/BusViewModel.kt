package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.local.busSchedule.BusScheduleRepository
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor() : ViewModel() {
    private val _busLines = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val busLines: StateFlow<List<BusDetailDomain>> = _busLines

    init {
        // Inicia el temporizador para limpiar los autobuses viejos cada minuto
        startBusCleanupTimer()
    }

    // Método para inicializar los detalles de una línea específica de autobús
    fun initBusDetails(busLineId: String) {
        viewModelScope.launch {
            checkBusDepartures(busLineId)
        }
    }

    // Método para verificar las salidas de autobuses
    private fun checkBusDepartures(busLineId: String) {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
        val currentHour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime.time)

        val activeBuses = BusScheduleRepository.busSchedules
            .filter { it.line == busLineId }
            .flatMap { schedule ->
                val lineSchedule = when {
                    checkIfHoliday() -> schedule.holidaySchedule
                    currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY -> schedule.saturdaySchedule
                    else -> schedule.normalSchedule
                }

                lineSchedule.filter { it == currentHour }.map { time ->
                    // Crea la hora de salida
                    val departureTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
                    departureTime.set(Calendar.HOUR_OF_DAY, SimpleDateFormat("HH", Locale.getDefault()).format(SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time)!!).toInt())
                    departureTime.set(Calendar.MINUTE, SimpleDateFormat("mm", Locale.getDefault()).format(SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time)!!).toInt())

                    // Guarda el tiempo de creación y la hora de salida
                    BusDetailDomain(line = schedule.line, time = time, isFull = false, departureTime = departureTime)
                }
            }

        // Agrega autobuses activos a la lista
        if (activeBuses.isNotEmpty()) {
            _busLines.update { it + activeBuses }
        }
    }

    // Temporizador de limpieza
    private fun startBusCleanupTimer() {
        viewModelScope.launch {
            while (true) {
                cleanUpOldBuses()
                delay(TimeUnit.MINUTES.toMillis(1)) // Cambia a un minuto para limpieza más frecuente
            }
        }
    }

    // Elimina autobuses cuya hora de salida ha pasado hace más de una hora
    private fun cleanUpOldBuses() {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))

        _busLines.update { buses ->
            buses.filter { busDetail ->
                // Verifica si la hora de salida del autobús es dentro de la última hora
                val difference = currentTime.timeInMillis - busDetail.departureTime.timeInMillis
                difference <= TimeUnit.HOURS.toMillis(1)
            }
        }
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

    // Método para verificar si es un día festivo
    private fun checkIfHoliday(): Boolean {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }
}
