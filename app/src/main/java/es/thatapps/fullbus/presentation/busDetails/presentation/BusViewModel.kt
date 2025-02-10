package es.thatapps.fullbus.presentation.busDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.thatapps.fullbus.data.local.busSchedule.BusScheduleRepository
import es.thatapps.fullbus.data.repository.AuthRepository
import es.thatapps.fullbus.data.repository.BusRepository
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val busRepository: BusRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    // Configuración de la zona horaria y formato de hora
    private val madridTimeZone = TimeZone.getTimeZone("Europe/Madrid")
    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()).apply { timeZone = madridTimeZone }
    private val calendar = Calendar.getInstance(madridTimeZone)
    private val currentTime = sdf.format(calendar.time)
    private val resetHour = 4
    private val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    // Estado de los buses activos
    private val _activeBuses = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val activeBuses: StateFlow<List<BusDetailDomain>> = _activeBuses

    // Función para obtener el día actual
    private fun getLogicDay(): Int {
        // Crea una copia del calendario para no modificar el original
        val adjustedCalendar = (calendar.clone() as Calendar).apply {
            if (currentHour < resetHour) {
                add(Calendar.DAY_OF_YEAR, -1) // Retrocede un día si es antes de las 4:00 am
            }
        }
        return adjustedCalendar.get(Calendar.DAY_OF_WEEK) // Devuelve el día lógico
    }

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
        // Primero elimina todos los buses expirados
        deleteBus()

        // Obtiene los buses ya activos en Firestore, utilizando un Map para una búsqueda rápida
        val existingBuses = _activeBuses.value.associateBy { it.departureTime }

        // Crea nuevos buses según el horario actual si no existen
        BusScheduleRepository.busSchedules.forEach { busSchedule ->
            val schedule = when (getLogicDay()) {
                Calendar.SATURDAY -> busSchedule.schedules["Saturday"]
                Calendar.SUNDAY -> busSchedule.schedules["Holiday"]
                else -> busSchedule.schedules["Normal"]
            }

            // Verifica si el bus debe ser creado comparando la hora actual con la hora de salida, y si ya existe
            schedule?.forEach { (direction, times) ->
                times.forEach { departureTime ->
                    val arrivalTime = calculateArrivalTime(departureTime)
                    if (shouldCreateBus(departureTime, arrivalTime, currentTime) && !existingBuses.containsKey(departureTime)) {
                        busRepository.addBus(
                            BusDetailDomain(
                                line = busSchedule.line,
                                departureTime = departureTime,
                                arriveTime = arrivalTime,
                                direction = direction,
                                isFull = false,
                                id = "${busSchedule.line}_${departureTime}_${direction}",
                                day = getLogicDay().toString(),
                            )
                        )
                    }
                }
            }
        }
        loadActiveBuses()
    }

    private suspend fun deleteBus() {
        _activeBuses.value.forEach { bus ->
            // Compara la hora actual con la de llegada del bus y el dia actual, si es mayor lo elimina
            if (currentTime > bus.arriveTime || bus.day != getLogicDay().toString()) {
                busRepository.deleteBus(bus)
            }
        }
    }

    // Verifica si el bus debe ser creado según el rango de horarios
    private fun shouldCreateBus(departureTime: String, arrivalTime: String, currentTime: String): Boolean {
        return if (departureTime > arrivalTime) {
            // Si el rango cruza la medianoche
            currentTime in departureTime.. "23:59" || currentTime in "00:00"..arrivalTime
        } else {
            // Rango normal, dentro del mismo día
            currentTime in departureTime..arrivalTime
        }
    }

    // Calcula la hora de llegada sumando 1 hora a la hora de salida
    private fun calculateArrivalTime(departureTime: String): String {
        val departureDate = sdf.parse(departureTime) ?: return departureTime
        val arrivalDate = Date(departureDate.time + (60 * 60 * 1000)) // Añade 1 hora
        return sdf.format(arrivalDate)
    }

    // Función para establecer el bus como lleno
    fun reportFull(busId: String) {
        viewModelScope.launch {
            // Encuentra el bus en la lista de buses activos
            _activeBuses.value = _activeBuses.value.map { bus ->
                if (bus.id == busId) {
                    bus.copy(isFull = true) // Crea una copia del bus con isFull como true
                } else {
                    bus
                }
            }
            // Luego actualiza el estado del bus en Firestore
            busRepository.updateBus(_activeBuses.value.find { it.id == busId }!!)

            // Llama a refreshActiveBuses para recargar los buses activos si es necesario
            refreshActiveBuses()
        }
    }

    fun logout() {
        authRepository.logout()
    }
}