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
                adUnitId = "ca-app-pub-5425722635628040/3387020168" // ID de unidad de anuncio
                loadAd(AdRequest.Builder().build())
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}