package es.thatapps.fullbus.presentation.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.thatapps.fullbus.R
import es.thatapps.fullbus.presentation.components.AdBanner
import es.thatapps.fullbus.presentation.components.BusInfoBox
import es.thatapps.fullbus.presentation.components.BusStatusBox
import es.thatapps.fullbus.presentation.components.Header

@Composable
fun MainScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Encabezado con la información de la línea de autobús y el logo
        Header()

        Spacer(modifier = Modifier.width(15.dp))

        BusInfoBox(
            lineNumber = "M-126",
            backgroundColor = Color(0xFFFFC0CB), // Color rosa
            origin = "Viso",
            destination = "Sevilla",
            busIcon = R.drawable.bus_1
        )
    }
}