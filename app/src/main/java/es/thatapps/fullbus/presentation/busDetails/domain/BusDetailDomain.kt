package es.thatapps.fullbus.presentation.busDetails.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class BusDetailDomain(
    val line: String,
    val time: String,
    var isFull: Boolean,
    val createdAt: Long = System.currentTimeMillis(), // Timestamp de creación
    val departureTime: Calendar // Hora de salida del autobús
) {
    init {
        // Establece la hora de salida basado en el campo `time`
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedTime = sdf.parse(time) ?: throw IllegalArgumentException("Invalid time format")

        departureTime.time = parsedTime
        departureTime.timeZone = TimeZone.getTimeZone("Europe/Madrid")
    }
}
