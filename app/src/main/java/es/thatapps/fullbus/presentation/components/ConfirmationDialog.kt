package es.thatapps.fullbus.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    confirmText: String,
    dismissText: String = "Cancelar",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(dismissText)
                }
            }
        )
    }
}