package es.thatapps.fullbus.presentation.busDetails.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class BusDetailDomain(
    val line: String = "",
    val time: String = "",
    var isFull: Boolean = false,
    var departureTime: String = ""
) {
    // Calcula arriveTime dinámicamente una hora después que departureTime
    val arriveTime: String
        get() {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val departureDate = sdf.parse(departureTime) ?: throw IllegalArgumentException("Invalid time format")
            val calendar = Calendar.getInstance().apply {
                time = departureDate
                add(Calendar.HOUR_OF_DAY, 1)
            }
            return sdf.format(calendar.time)  // Regresa como String la hora de llegada
        }

    init {
        if (time.isNotEmpty()) {
            // Establece departureTime basado en el campo time (convertido a String con formato HH:mm)
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val parsedTime = sdf.parse(time) ?: throw IllegalArgumentException("Invalid time format")
            departureTime = sdf.format(parsedTime)  // Guardamos solo como String (hora y minutos)
        }
    }
}
