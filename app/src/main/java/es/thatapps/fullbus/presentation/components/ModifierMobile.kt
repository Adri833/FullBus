package es.thatapps.fullbus.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.ui.composed

// Modificador personalizado para ajustar el contenido a la pantalla del dispositivo
fun Modifier.adjustForMobile(): Modifier {
    return composed {
        val insets = WindowInsets
        val density = LocalDensity.current

        Modifier.padding(
            bottom = with(density) { insets.navigationBars.asPaddingValues().calculateBottomPadding() }
        )
    }
}
