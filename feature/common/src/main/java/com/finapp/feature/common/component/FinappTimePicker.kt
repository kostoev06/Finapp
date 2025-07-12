package com.finapp.feature.common.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinappTimePicker(
    isShown: Boolean,
    initialTime: LocalTime,
    onConfirm: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (!isShown) return

    val timePickerState = rememberTimePickerState(
        initialHour   = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour      = true
    )

    TimePickerDialog(
        onDismiss = onDismissRequest,
        onConfirm = {
            onConfirm(LocalTime.of(timePickerState.hour, timePickerState.minute))
        }
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        text = { content() }
    )
}