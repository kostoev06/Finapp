package com.finapp.feature.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.theme.GreenPrimaryLight
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinappDatePicker(
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    onDateConfirm: (LocalDate) -> Unit,
    initialDate: LocalDate
) {
    if (!isShown) return

    val dateMillis = remember(initialDate) {
        initialDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selected = pickerState.selectedDateMillis ?: return@TextButton
                val localDate = Instant
                    .ofEpochMilli(selected)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                onDateConfirm(localDate)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        colors = DatePickerDefaults.colors(
            headlineContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            subheadContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            dividerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
            dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            weekdayContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        DatePicker(
            state = pickerState,
            modifier = Modifier
                .padding(bottom = 8.dp),
            colors = DatePickerDefaults.colors(
                headlineContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                subheadContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dividerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                weekdayContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}
