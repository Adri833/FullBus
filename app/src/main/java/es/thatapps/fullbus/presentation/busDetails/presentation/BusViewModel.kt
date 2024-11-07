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
            loadActiveBuses()
            createBusesBasedOnSchedule()
        }
    }

    // Carga los buses activos desde Firestore y actualiza el estado
    private suspend fun loadActiveBuses() {
        _activeBuses.value = busRepository.getActiveBuses()
    }

    // Crea buses según el horario y día actual, evitando duplicados
    private suspend fun createBusesBasedOnSchedule() {
        val currentDate = Calendar.getInstance(madridTimeZone)
        val currentTimeString = sdf.format(Date())

        // Obtenemos los buses ya activos en Firestore, utilizando un Map para una búsqueda rápida
        val existingBuses = busRepository.getActiveBuses().associateBy { it.departureTime }

        // Iteramos sobre los horarios de buses para crear instancias
        BusScheduleRepository.busSchedules.forEach { busSchedule ->
            val schedule = when (currentDate.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SATURDAY -> busSchedule.saturdaySchedule
                Calendar.SUNDAY -> busSchedule.holidaySchedule
                else -> busSchedule.normalSchedule
            }

            schedule.forEach { departureTime ->
                // Verificamos si el bus debe ser creado
                if (shouldCreateBus(departureTime, currentTimeString, existingBuses)) {
                    val arrivalTime = calculateArrivalTime(departureTime)
                    val busDetail = BusDetailDomain(
                        line = busSchedule.line,
                        departureTime = departureTime,
                        arriveTime = arrivalTime,
                        isFull = false
                    )

                    // Añadimos el bus si aún no existe
                    busRepository.addBus(busDetail)
                }
            }
        }

        // Carga los buses activos después de añadir los nuevos
        loadActiveBuses()
    }

    // Verifica si el bus debe ser creado
    private fun shouldCreateBus(departureTime: String, currentTimeString: String, existingBuses: Map<String, BusDetailDomain>): Boolean {
        val departureDate = sdf.parse(departureTime) ?: return false
        val currentDate = sdf.parse(currentTimeString) ?: return false

        // Calculamos la hora de llegada para el bus
        val arrivalTime = calculateArrivalTime(departureTime)
        val arrivalDate = sdf.parse(arrivalTime) ?: return false

        // El bus debe ser creado si la hora actual es entre la hora de salida y la hora de llegada
        return currentDate >= departureDate && currentDate <= arrivalDate && !existingBuses.containsKey(departureTime)
    }

    // Calcula la hora de llegada sumando 1 hora a la hora de salida
    private fun calculateArrivalTime(departureTime: String): String {
        val departureDate = sdf.parse(departureTime) ?: return departureTime
        val arrivalDate = Date(departureDate.time + 60 * 60 * 1000) // Añade 1 hora
        return sdf.format(arrivalDate)
    }

    // Verifica y carga los buses activos en el estado, eliminando los que ya han pasado su hora de llegada
    // Verifica y carga los buses activos en el estado, pero sin eliminar buses
    fun checkActiveBuses() {
        val currentTime = System.currentTimeMillis()
        val activeBuses = _activeBuses.value

        viewModelScope.launch {
            // Filtra los buses activos
            val busesToKeep = activeBuses.filter { bus ->
                val departureDate = sdf.parse(bus.departureTime) ?: return@filter false
                val arriveDate = sdf.parse(bus.arriveTime) ?: return@filter false
                val currentDate = Date(currentTime)

                // Compara la hora actual con la hora de salida y la hora de llegada
                currentDate.after(departureDate) && currentDate.before(arriveDate)
            }

            // Actualiza el estado de los buses activos
            _activeBuses.value = busesToKeep
        }
    }

    // Mueve la lógica de borrado a un método separado
    fun removeExpiredBuses() {
        val currentTime = System.currentTimeMillis()

        viewModelScope.launch {
            val activeBuses = _activeBuses.value

            // Elimina los buses que han pasado su hora de llegada
            activeBuses.forEach { bus ->
                val arriveDate = sdf.parse(bus.arriveTime) ?: return@forEach
                val currentDate = Date(currentTime)

                // Si la hora de llegada ya ha pasado, elimina el bus de Firestore
                if (currentDate.after(arriveDate)) {
                    busRepository.deleteBus(bus)
                }
            }
        }
    }

    // Función para poner el bus como lleno
    fun reportFull(busLineId: String) {
        viewModelScope.launch {
            _activeBuses.value.find { it.line == busLineId }?.let { bus ->
                bus.isFull = true
                busRepository.updateBus(bus) // Actualiza el estado del bus en Firestore
                checkActiveBuses() // Recarga los buses activos
            }
        }
    }

    // Función para poner el bus como disponible
    fun setBusAvailable(busLine: String) {
        viewModelScope.launch {
            _activeBuses.value.find { it.line == busLine }?.let { bus ->
                bus.isFull = false
                busRepository.updateBus(bus)  // Actualiza el estado del bus en Firestore
                checkActiveBuses()  // Recarga los buses activos
            }
        }
    }
}