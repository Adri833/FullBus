package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.local.busSchedule.BusScheduleRepository
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

    // Configura la zona horaria de Madrid
    private val madridTimeZone = TimeZone.getTimeZone("Europe/Madrid")
    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()).apply { timeZone = madridTimeZone }

    // Estado de los buses activos
    private val _activeBuses = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val activeBuses: StateFlow<List<BusDetailDomain>> = _activeBuses

    init {
        viewModelScope.launch {
            loadActiveBuses() //  Carga inicial de buses activos
            refreshActiveBuses() // Elimina buses expirados y crea nuevos
        }
    }

    // Carga los buses activos desde Firestore
    private suspend fun loadActiveBuses() {
        _activeBuses.value = busRepository.getActiveBuses()
    }

    // Refresca los buses activos (elimina expirados y crea nuevos)
    private suspend fun refreshActiveBuses() {
        val currentTimeMillis = System.currentTimeMillis()  // Obtén el tiempo actual en milisegundos
        val currentDate = Calendar.getInstance(madridTimeZone)
        val currentTimeString = sdf.format(currentDate.time)

        // Filtra y elimina los buses expirados directamente
        _activeBuses.value.forEach { bus ->
            val arriveDate = sdf.parse(bus.arriveTime) ?: return@forEach
            val arrivalTimeMillis = arriveDate.time  // La hora de llegada del bus en milisegundos

            // Si el bus ha expirado, lo eliminamos
            if (currentTimeMillis > arrivalTimeMillis) {
                busRepository.deleteBus(bus)  // Elimina el bus de Firestore
            }
        }

        // Recarga la lista de buses activos después de eliminar los expirados
        _activeBuses.value = busRepository.getActiveBuses()

        // Obtiene los buses ya activos en Firestore, utilizando un Map para una búsqueda rápida
        val existingBuses = _activeBuses.value.associateBy { it.departureTime }

        // Crea nuevos buses según el horario actual si no existen
        BusScheduleRepository.busSchedules.forEach { busSchedule ->
            val schedule = when (currentDate.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SATURDAY -> busSchedule.saturdaySchedule
                Calendar.SUNDAY -> busSchedule.holidaySchedule
                else -> busSchedule.normalSchedule
            }

            schedule.forEach { departureTime ->
                // Verificamos si el bus debe ser creado
                if (shouldCreateBus(departureTime, currentTimeString, existingBuses)) {
                    val newBus = BusDetailDomain(
                        line = busSchedule.line,
                        departureTime = departureTime,
                        arriveTime = calculateArrivalTime(departureTime),
                        isFull = false
                    )
                    busRepository.addBus(newBus) // Crea el bus en Firestore
                }
            }
        }
        // Recarga la lista de buses activos
        loadActiveBuses()
    }

    // Verifica si el bus debe ser creado
    private fun shouldCreateBus(
        departureTime: String,
        currentTimeString: String,
        existingBuses: Map<String, BusDetailDomain>
    ): Boolean {
        val departureDate = sdf.parse(departureTime) ?: return false
        val currentDate = sdf.parse(currentTimeString) ?: return false

        // Calcula la hora de llegada y verifica que el bus no exista ya y que esté dentro del rango de tiempo
        val arrivalDate = Date(departureDate.time + 3600000) // Añadir 1 hora
        return currentDate in departureDate..arrivalDate && !existingBuses.containsKey(departureTime)
    }

    // Calcula la hora de llegada sumando 1 hora a la hora de salida
    private fun calculateArrivalTime(departureTime: String): String {
        val departureDate = sdf.parse(departureTime) ?: return departureTime
        val arrivalDate = Date(departureDate.time + 60 * 60 * 1000) // Añade 1 hora
        return sdf.format(arrivalDate)
    }

    // Función para poner el bus como lleno
    fun reportFull(busLineId: String) {
        viewModelScope.launch {
            _activeBuses.value.find { it.line == busLineId }?.let { bus ->
                bus.isFull = true
                busRepository.updateBus(bus) // Actualiza el estado del bus en Firestore
                refreshActiveBuses() // Recarga los buses activos
            }
        }
    }

    // Función para poner el bus como disponible
    fun setBusAvailable(busLine: String) {
        viewModelScope.launch {
            _activeBuses.value.find { it.line == busLine }?.let { bus ->
                bus.isFull = false
                busRepository.updateBus(bus)  // Actualiza el estado del bus en Firestore
                refreshActiveBuses()  // Recarga los buses activos
            }
        }
    }
}