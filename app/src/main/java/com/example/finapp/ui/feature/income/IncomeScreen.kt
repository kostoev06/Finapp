package com.example.finapp.ui.feature.income

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.ui.common.HomeTopBar
import com.example.finapp.ui.feature.income.viewmodel.IncomeViewModel
import com.example.finapp.ui.common.FinappFAB
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.finapp.R
import com.example.finapp.ui.common.FinappListItem
import com.example.finapp.ui.feature.income.viewmodel.IncomeViewModelFactory
import com.example.finapp.ui.utils.currencySymbol

@Composable
fun IncomeRoute(
    viewModel: IncomeViewModel = viewModel(factory = IncomeViewModelFactory()),
    onClickHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    IncomeScreen(
        state = state,
        modifier = modifier,
        onClickHistory = onClickHistory
    )
}

@Composable
fun IncomeScreen(
    state: IncomeScreenUiState,
    onClickHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    IncomeContent(
        state = state,
        onClickHistory = onClickHistory,
        modifier = modifier
    )
}

@Composable
fun IncomeContent(
    state: IncomeScreenUiState,
    onClickHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
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
        floatingActionButton = { FinappFAB() },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                title = "Всего",
                firstTrailingContent = {
                    Text(
                        stringResource(
                            R.string.amount_with_currency,
                            state.summary.totalAmount,
                            currencySymbol(state.currency)
                        )
                    )
                },
                green = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        title = item.title,
                        firstTrailingContent = {
                            Text(
                                stringResource(
                                    R.string.amount_with_currency,
                                    item.amount,
                                    currencySymbol(state.currency)
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
                        onClick = { }
                    )
                }
            }
        }
    }
}
