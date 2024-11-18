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

    // Variables de tiempo
    private val madridTimeZone = TimeZone.getTimeZone("Europe/Madrid")
    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault()).apply { timeZone = madridTimeZone }
    private val sdfDay = SimpleDateFormat("EEEE", Locale("es", "ES")).apply { timeZone = madridTimeZone }
    private val calendar = Calendar.getInstance(madridTimeZone)
    private val currentTime = sdf.format(calendar.time)
    private val resetHour = 4
    private val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    // Estado de los buses activos
    private val _activeBuses = MutableStateFlow<List<BusDetailDomain>>(emptyList())
    val activeBuses: StateFlow<List<BusDetailDomain>> = _activeBuses

    // Función para obtener la hora actual
    fun getCurrentHour(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply { timeZone = madridTimeZone }.format(Date())
    }

    // Función para obtener el día actual
    fun getLogicDay(): String {
        // Crea una copia de calendar para evitar modificar el estado original
        val adjustedCalendar = (calendar.clone() as Calendar).apply {
            if (currentHour < resetHour) {
                add(Calendar.DAY_OF_YEAR, -1) // Resta un día si es antes de las 4:00 am
            }
        }
        // Usa la copia ajustada para obtener el día lógico
        return sdfDay.format(adjustedCalendar.time)
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
        _activeBuses.value.forEach { bus ->
            // Si la hora actual es mayor o igual a 4:00, el dia se considera
            if (currentHour < resetHour) {
                calendar.add(Calendar.DAY_OF_YEAR, -1) // Resta un dia
            }

            // Establece las 4:00 como la hora de inicio del nuevo dia
            calendar.set(Calendar.HOUR_OF_DAY, resetHour)

            // Compara la hora actual con la de llegada del bus, si es mayor lo elimina
            if (currentTime > bus.arriveTime || isPreviousDay(bus.departureTime, currentTime)) {
                busRepository.deleteBus(bus)
            }
        }

        // Obtiene los buses ya activos en Firestore, utilizando un Map para una búsqueda rápida
        val existingBuses = _activeBuses.value.associateBy { it.departureTime }

        // Crea nuevos buses según el horario actual si no existen
        BusScheduleRepository.busSchedules.forEach { busSchedule ->
            val schedule = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SATURDAY -> busSchedule.saturdaySchedule
                Calendar.SUNDAY -> busSchedule.holidaySchedule
                else -> busSchedule.normalSchedule
            }

            // Verifica si el bus debe ser creado comparando la hora actual con la hora de salida, y si ya existe
            schedule.forEach { departureTime ->
                if (currentTime in departureTime..calculateArrivalTime(departureTime) && !existingBuses.containsKey(departureTime)) {
                    busRepository.addBus(BusDetailDomain(
                            line = busSchedule.line,
                            departureTime = departureTime,
                        arriveTime = calculateArrivalTime(departureTime),
                            isFull = false
                        )
                    )
                }
            }
        }
        // Recarga la lista de buses activos
        loadActiveBuses()
    }

    // Verifica si el autobús pertenece a un día anterior
    private fun isPreviousDay(departureTime: String, currentTime: String): Boolean {
        val departureDate = sdf.parse(departureTime) ?: return false
        val currentDate = sdf.parse(currentTime) ?: return false

        return departureDate.before(currentDate) && currentTime < "04:00" // Nuevo día desde las 4:00 am
    }

    // Calcula la hora de llegada sumando 1 hora a la hora de salida
    private fun calculateArrivalTime(departureTime: String): String {
        val departureDate = sdf.parse(departureTime) ?: return departureTime
        val arrivalDate = Date(departureDate.time + (60 * 60 * 1000)) // Añade 1 hora
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
}