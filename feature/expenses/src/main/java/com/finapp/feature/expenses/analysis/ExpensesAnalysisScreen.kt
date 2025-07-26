package com.finapp.feature.expenses.analysis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finapp.feature.charts.component.DonutChart
import com.finapp.feature.common.component.FinappDatePicker
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.utils.currencySymbolRes
import com.finapp.feature.expenses.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ExpensesAnalysisRoute(
    viewModel: ExpensesAnalysisViewModel = viewModel(),
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExpensesAnalysisUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = "${event.title}: ${event.message}"
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        ExpensesAnalysisScreen(
            state = state,
            onClickBack = onClickBack,
            onChooseStartDate = viewModel::onChooseStartDate,
            onChooseEndDate = viewModel::onChooseEndDate,
            modifier = modifier.padding(it)
        )
    }

}

@Composable
fun ExpensesAnalysisScreen(
    state: ExpensesAnalysisScreenUiState,
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

    ExpensesAnalysisContent(
        state = state,
        onClickBack = onClickBack,
        onClickStartDate = { showStartPicker = true },
        onClickEndDate = { showEndPicker = true },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesAnalysisContent(
    state: ExpensesAnalysisScreenUiState,
    onClickBack: () -> Unit,
    onClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backButtonEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.analysis)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                            backButtonEnabled = false
                        },
                        enabled = backButtonEnabled
                    ) {
                        Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = null)
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.start)) },
                firstTrailingContent = {
                    SuggestionChip(
                        onClick = onClickStartDate,
                        label = {
                            Text(
                                text = state.startDate.format(
                                    DateTimeFormatter.ofPattern(
                                        "d MMMM yyyy",
                                        Locale("ru")
                                    )
                                ),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.height(36.dp)
                    )
                },
                height = 56
            )
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.end)) },
                firstTrailingContent = {
                    SuggestionChip(
                        onClick = onClickEndDate,
                        label = {
                            Text(
                                text = state.endDate.format(
                                    DateTimeFormatter.ofPattern(
                                        "d MMMM yyyy",
                                        Locale("ru")
                                    )
                                ),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.height(36.dp)
                    )
                },
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
                height = 56
            )

            if (!state.isLoading && state.items.isNotEmpty()) {
                val slices = remember(state.items) { state.items.toPieSlices() }

                DonutChart(
                    slices = slices,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    strokeWidth = 9.dp,
                    labelTextSizeSp = 7f,
                    maxLegendItems = 6,
                    radiusFraction = 0.35f,
                    legendItemSpacing = 1.dp,
                    legendDotSize = 5.dp
                )
            }

            HorizontalDivider()

            if (state.isLoading) {
                LoadingContent(modifier = modifier)
            } else {
                if (state.items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_data_for_period),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn {
                        items(state.items) { item ->
                            FinappListItem(
                                leadingSymbols = item.leadingSymbols,
                                headlineContent = { Text(item.title) },
                                firstTrailingContent = {
                                    Text(
                                        stringResource(
                                            R.string.percentage_format,
                                            item.percent
                                        )
                                    )
                                },
                                secondTrailingContent = {
                                    Text(
                                        stringResource(
                                            R.string.amount_with_currency,
                                            item.amount,
                                            stringResource(
                                                currencySymbolRes(state.currency)
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
