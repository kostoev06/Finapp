package com.finapp.feature.income.homepage

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.finapp.feature.common.component.FinappFAB
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.utils.currencySymbolRes
import com.finapp.feature.income.R

@Composable
fun IncomeRoute(
    viewModel: IncomeViewModel = viewModel(),
    onClickHistory: () -> Unit,
    onEditIncome: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadIncome()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    IncomeScreen(
        state = state,
        modifier = modifier,
        onEditIncome = onEditIncome,
        onClickHistory = onClickHistory
    )
}

@Composable
fun IncomeScreen(
    state: IncomeScreenUiState,
    onClickHistory: () -> Unit,
    onEditIncome: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    IncomeContent(
        state = state,
        onClickHistory = onClickHistory,
        onEditIncome = onEditIncome,
        modifier = modifier
    )
}

@Composable
fun IncomeContent(
    state: IncomeScreenUiState,
    onClickHistory: () -> Unit,
    onEditIncome: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.income_top_title)) },
                actions = {
                    IconButton(onClick = { onClickHistory() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = { FinappFAB(onClick = { onEditIncome(null) }) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.total)) },
                firstTrailingContent = {
                    Text(
                        stringResource(
                            R.string.amount_with_currency,
                            state.summary.totalAmount,
                            stringResource(
                                currencySymbolRes(state.currency)
                            )
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colored = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        headlineContent = { Text(item.title) },
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
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right_1),
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
