package com.example.finapp.ui.home.income.history.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.R
import com.example.finapp.ui.common.FinappDatePicker
import com.example.finapp.ui.home.common.HomeTopBar
import com.example.finapp.ui.home.income.history.viewmodel.IncomeHistoryViewModel
import com.example.finapp.ui.common.FinappListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun IncomeHistoryRoute(
    viewModel: IncomeHistoryViewModel = viewModel(),
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    IncomeHistoryScreen(
        state = state,
        onClickBack = onClickBack,
        onChooseStartDate = viewModel::onChooseStartDate,
        onChooseEndDate = viewModel::onChooseEndDate,
        modifier = modifier
    )
}

@Composable
fun IncomeHistoryScreen(
    state: IncomeHistoryScreenUiState,
    onClickBack: () -> Unit,
    onChooseStartDate: (LocalDate) -> Unit,
    onChooseEndDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    FinappDatePicker(
        isShown = showStartPicker,
        onDismissRequest = { showStartPicker = false },
        onDateConfirm = {
            onChooseStartDate(it)
            showStartPicker = false
        },
        initialDate = state.startDate
    )

    FinappDatePicker(
        isShown = showEndPicker,
        onDismissRequest = { showEndPicker = false },
        onDateConfirm = {
            onChooseEndDate(it)
            showEndPicker = false
        },
        initialDate = state.endDate
    )

    IncomeHistoryContent(
        state = state,
        onClickBack = onClickBack,
        onClickStartDate = { showStartPicker = true },
        onClickEndDate = { showEndPicker = true },
        modifier = modifier
    )
}

@Composable
fun IncomeHistoryContent(
    state: IncomeHistoryScreenUiState,
    onClickBack: () -> Unit,
    onClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text("Моя история") },
                navigationIcon = {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_analysis),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                title = "Начало",
                firstTrailingContent = {
                    Text(state.startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                },
                green = true,
                clickable = true,
                onClick = onClickStartDate,
                height = 56
            )
            FinappListItem(
                title = "Конец",
                firstTrailingContent = {
                    Text(state.endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                },
                green = true,
                clickable = true,
                onClick = onClickEndDate,
                height = 56
            )
            FinappListItem(
                title = "Сумма",
                firstTrailingContent = { Text(state.summary.totalFormatted) },
                green = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        title = item.title,
                        subtitle = item.subtitle,
                        firstTrailingContent = { Text(item.amountFormatted) },
                        secondTrailingContent = { Text(item.timeText) },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right_1),
                                contentDescription = null
                            )
                        },
                        clickable = true
                    )
                }
            }
        }
    }
}