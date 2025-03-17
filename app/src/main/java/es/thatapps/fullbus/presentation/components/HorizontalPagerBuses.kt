package es.thatapps.fullbus.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.thatapps.fullbus.presentation.busDetails.domain.BusDetailDomain
import es.thatapps.fullbus.presentation.busDetails.presentation.BusViewModel
import es.thatapps.fullbus.utils.getPFP
import kotlinx.coroutines.launch

@Composable
fun HorizontalPagerBuses(
    activeBuses: List<BusDetailDomain>,
    viewModel: BusViewModel
) {
    val pagerState = rememberPagerState(pageCount = { activeBuses.size })
    val coroutineScope = rememberCoroutineScope()
    var pfp by remember { mutableStateOf<String?>(null) }
    var pfpbus by remember { mutableStateOf<String?>(null) }
    var userbus by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(activeBuses, pagerState.currentPage) {
        if (activeBuses.isNotEmpty()) {
            val bus = activeBuses[pagerState.currentPage]
            try {
                pfp = getPFP()
                username = viewModel.getUsername()
                userbus = viewModel.getUserBus(bus)
                pfpbus = viewModel.getPFPBus(bus)
            } catch (_: Exception) {
            }
        }
    }


    Box(modifier = Modifier.fillMaxWidth()) {
        // HorizontalPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 16.dp
        ) { page ->
            val bus = activeBuses[page]

            // Animacion de escala
            val scale by animateFloatAsState(
                targetValue = if (pagerState.currentPage == page) 1f else 0.9f, label = "",
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale) // Aplica la animacion de escala
            ) {
                BusStatus(
                    busDetail = bus,
                    onReportFull = { showDialog = true },
                    pfp = pfpbus.toString(),
                    username = userbus.toString()
                )
            }

            if (showDialog) {
                ConfirmationDialog(
                    showDialog = showDialog,
                    title = "Reportar bus lleno",
                    message = "¿Seguro que quieres reportar el bus como lleno?",
                    confirmText = "Reportar",
                    onConfirm = { viewModel.reportFull(bus.id, username.toString(), pfp.toString()) ; showDialog = false },
                    onDismiss = { showDialog = false }
                )
            }
        }

        // Flecha izquierda
        if (pagerState.currentPage > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                        }
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Página anterior",
                    tint = Color.Black,
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        // Flecha derecha
        if (pagerState.currentPage < activeBuses.size - 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(activeBuses.size - 1))
                        }
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Página siguiente",
                    tint = Color.Black,
                    modifier = Modifier.size(54.dp)
                )
            }
        }
    }
}