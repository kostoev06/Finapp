package com.example.finapp.ui.home.expenses.component

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.finapp.ui.home.common.HomeTopBar
import com.example.finapp.R
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl
import com.example.finapp.ui.home.expenses.viewmodel.ExpensesViewModel
import com.example.finapp.ui.common.FinappFAB
import com.example.finapp.ui.common.FinappListItem
import com.example.finapp.ui.home.expenses.viewmodel.ExpensesViewModelFactory


@Composable
fun ExpensesRoute(
    viewModel: ExpensesViewModel = viewModel(factory = ExpensesViewModelFactory()),
    modifier: Modifier = Modifier,
    onClickHistory: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    ExpensesScreen(
        state = state,
        modifier = modifier,
        onClickHistory = onClickHistory
    )
}

@Composable
fun ExpensesScreen(
    state: ExpensesScreenUiState,
    modifier: Modifier = Modifier,
    onClickHistory: () -> Unit
) {
    ExpensesContent(
        state = state,
        modifier = modifier,
        onClickHistory = onClickHistory
    )
}

@Composable
fun ExpensesContent(
    state: ExpensesScreenUiState,
    modifier: Modifier = Modifier,
    onClickHistory: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text("Расходы сегодня") },
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
        floatingActionButton = { FinappFAB() },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                title = "Всего",
                firstTrailingContent = { Text(state.summary.totalFormatted) },
                green = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        leadingSymbols = item.leadingSymbols,
                        title = item.title,
                        subtitle = item.subtitle,
                        firstTrailingContent = { Text(item.amountFormatted) },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right_1),
                                contentDescription = null
                            )
                        },
                        clickable = true,
                        onClick = { }
                    )
                }
            }
        }
    }
}
