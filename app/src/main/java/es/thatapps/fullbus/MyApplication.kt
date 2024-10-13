package es.thatapps.fullbus

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

/*
@HiltAndroidApp indica que tu aplicación Android será el punto de entrada de la inyección de dependencias.
Su función principal es configurar Hilt en toda la aplicación.
 */

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
    }
}