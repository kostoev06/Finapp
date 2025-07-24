package com.finapp.feature.account.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.finapp.feature.account.R
import com.finapp.feature.charts.Chart
import com.finapp.feature.charts.model.ChartEntry
import com.finapp.feature.charts.model.ChartType
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.theme.GreenPrimaryLight
import com.finapp.feature.common.utils.currencySymbolRes


@Composable
fun AccountRoute(
    viewModel: AccountViewModel = viewModel(),
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadAccount()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AccountUiEvent.ShowError -> {
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
        AccountScreen(
            state = state,
            onClickEdit = onClickEdit,
            modifier = modifier.padding(it)
        )
    }
}

@Composable
fun AccountScreen(
    state: AccountScreenUiState,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AccountContent(
        state = state,
        onClickEdit = onClickEdit,
        modifier = modifier
    )
}

@Composable
fun AccountContent(
    state: AccountScreenUiState,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.account_top_title)) },
                actions = {
                    IconButton(onClick = onClickEdit) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
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
                leadingSymbols = "💰",
                headlineContent = { Text(stringResource(R.string.balance)) },
                firstTrailingContent = {
                    Text(
                        stringResource(
                            R.string.amount_with_currency,
                            state.balanceState.balance,
                            stringResource(
                                currencySymbolRes(state.currencyState.currency)
                            )
                        )
                    )
                },
                green = true,
                height = 56,
            )
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.currency)) },
                firstTrailingContent = {
                    Text(
                        stringResource(
                            currencySymbolRes(state.currencyState.currency)
                        )
                    )
                },
                green = true,
                height = 56
            )
            FinappListItem(
                headlineContent = { Text("Последняя синхронизация") },
                firstTrailingContent = { Text(state.lastSyncTextState) },
                height = 56
            )
            ProfitAnalysisChart(state)
        }
    }
}

@Composable
fun ProfitAnalysisChart(uiState: AccountScreenUiState) {
    var chartType by remember { mutableStateOf(ChartType.LINE) }
    val options = ChartType.entries
    val entries = uiState.profitItemListUiState.map {
        ChartEntry(label = it.title, value = it.amount)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { chartType = option },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = GreenPrimaryLight
                    ),
                    selected = option == chartType
                ) {
                    Text(text = option.name)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Chart(
            entries = entries,
            type = chartType,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            onEntrySelected = { entry ->
                entry?.let {

                }
            }
        )
    }
}
