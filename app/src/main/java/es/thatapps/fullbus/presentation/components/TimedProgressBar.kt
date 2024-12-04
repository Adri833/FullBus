package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import java.time.Duration
import java.time.LocalTime

@Composable
fun TimedProgressBar(
    startTime: String,
    endTime: String,
    currentTime: String
) {
    // Convierte los Strings en LocalTime
    val start = LocalTime.parse(startTime)
    val end = LocalTime.parse(endTime)
    val current = LocalTime.parse(currentTime)

    // Calcula la duracion total y el tiempo transcurrido
    val totalDuration = Duration.between(start, end).toMinutes().coerceAtLeast(1) // Evita divisiones entre 0
    val elapsed = Duration.between(start, current).toMinutes().coerceIn(0, totalDuration)
    val progress = elapsed.toFloat() / totalDuration

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = Color.Blue,
            trackColor = Color.LightGray,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }
}