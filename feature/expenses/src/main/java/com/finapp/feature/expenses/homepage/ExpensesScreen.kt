package com.finapp.feature.expenses.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.finapp.feature.common.component.FinappFAB
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.utils.currencySymbolRes
import com.finapp.feature.expenses.R


@Composable
fun ExpensesRoute(
    viewModel: ExpensesViewModel = viewModel(),
    onClickHistory: () -> Unit,
    onEditExpense: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadExpenses()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ExpensesScreen(
        state = state,
        modifier = modifier,
        onClickHistory = onClickHistory,
        onEditExpense = onEditExpense
    )
}

@Composable
fun ExpensesScreen(
    state: ExpensesScreenUiState,
    modifier: Modifier = Modifier,
    onClickHistory: () -> Unit,
    onEditExpense: (Long?) -> Unit,
) {
    ExpensesContent(
        state = state,
        modifier = modifier,
        onClickHistory = onClickHistory,
        onEditExpense = onEditExpense
    )
}

@Composable
fun ExpensesContent(
    state: ExpensesScreenUiState,
    modifier: Modifier = Modifier,
    onClickHistory: () -> Unit,
    onEditExpense: (Long?) -> Unit
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.expenses_top_title)) },
                actions = {
                    IconButton(onClick = onClickHistory) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = "История"
                        )
                    }
                }
            )
        },
        floatingActionButton = { FinappFAB(onClick = { onEditExpense(null) }) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                headlineContent = { Text("Всего") },
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
                        leadingSymbols = item.leadingSymbols,
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
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right_1),
                                contentDescription = null
                            )
                        },
                        clickable = true,
                        onClick = { onEditExpense(item.id) }
                    )
                }
            }
        }
    }
}
