package es.thatapps.fullbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import es.thatapps.fullbus.navigation.NavigationHost
import es.thatapps.fullbus.ui.theme.FullBusTheme
import com.google.android.gms.ads.MobileAds

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ocupa toda la pantalla posible
        MobileAds.initialize(this)
        setContent {
            val darkTheme = isSystemInDarkTheme()
            FullBusTheme(darkTheme) {
                NavigationHost()
            }
        }
    }

}