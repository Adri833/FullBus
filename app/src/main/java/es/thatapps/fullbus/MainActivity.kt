package es.thatapps.fullbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import es.thatapps.fullbus.navigation.NavigationHost
import es.thatapps.fullbus.ui.theme.FullBusTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ocupa toda la pantalla posible
        setContent {
            FullBusTheme {
                NavigationHost()
            }
        }
    }

}