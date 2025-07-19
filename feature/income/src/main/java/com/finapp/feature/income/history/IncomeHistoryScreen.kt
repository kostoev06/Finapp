package com.finapp.feature.income.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.feature.common.component.FinappDatePicker
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.utils.currencySymbolRes
import com.finapp.feature.income.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun IncomeHistoryRoute(
    viewModel: IncomeHistoryViewModel = viewModel(),
    onClickBack: () -> Unit,
    onEditIncome: (Long?) -> Unit,
    onAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadIncomeHistory()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    IncomeHistoryScreen(
        state = state,
        onClickBack = onClickBack,
        onChooseStartDate = viewModel::onChooseStartDate,
        onChooseEndDate = viewModel::onChooseEndDate,
        onEditIncome = onEditIncome,
        onAnalysis = onAnalysis,
        modifier = modifier
    )
}

@Composable
fun IncomeHistoryScreen(
    state: IncomeHistoryScreenUiState,
    onClickBack: () -> Unit,
    onChooseStartDate: (LocalDate) -> Unit,
    onChooseEndDate: (LocalDate) -> Unit,
    onEditIncome: (Long?) -> Unit,
    onAnalysis: () -> Unit,
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
        onEditIncome = onEditIncome,
        onAnalysis = onAnalysis,
        modifier = modifier
    )
}

@Composable
fun IncomeHistoryContent(
    state: IncomeHistoryScreenUiState,
    onClickBack: () -> Unit,
    onClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit,
    onEditIncome: (Long?) -> Unit,
    onAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backButtonEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text("Моя история") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                            backButtonEnabled = false
                        },
                        enabled = backButtonEnabled
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAnalysis() }) {
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
                headlineContent = { Text("Начало") },
                firstTrailingContent = {
                    Text(state.startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                },
                green = true,
                clickable = true,
                onClick = onClickStartDate,
                height = 56
            )
            FinappListItem(
                headlineContent = { Text("Конец") },
                firstTrailingContent = {
                    Text(state.endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                },
                green = true,
                clickable = true,
                onClick = onClickEndDate,
                height = 56
            )
            FinappListItem(
                headlineContent = { Text("Сумма") },
                firstTrailingContent = {
                    Text(
                        stringResource(
                            R.string.amount_with_currency,
                            state.summary.totalAmount,
                            stringResource(
                                currencySymbolRes(state.currency)
                            )
                        )
                    )
                },
                green = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        headlineContent = { Text(item.title) },
                        subtitle = item.subtitle,
                        firstTrailingContent = {
                            Text(
                                stringResource(
                                    R.string.amount_with_currency,
                                    item.amount,
                                    stringResource(
                                        currencySymbolRes(state.currency)
                                    )
                                )
                            )
                        },
                        secondTrailingContent = { Text(item.timeText) },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right_1),
                                contentDescription = null
                            )
                        },
                        clickable = true,
                        onClick = { onEditIncome(item.id) }
                    )
                }
            }
        }
    }
}
