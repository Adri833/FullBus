package es.thatapps.fullbus.presentation.components

import android.content.Context
import android.view.ViewGroup.LayoutParams
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdSize

@Composable
fun AdBanner(context: Context) {
    // Crear un AdView y cargar un anuncio
    AndroidView(
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // ID de unidad de anuncio TODO cambiar el ID del anuncio por el real
                loadAd(AdRequest.Builder().build())
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}