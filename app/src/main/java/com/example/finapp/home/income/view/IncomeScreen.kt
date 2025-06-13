package com.example.finapp.home.income.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.home.common.HomeTopBar
import com.example.finapp.home.income.viewmodel.IncomeViewModel
import com.example.finapp.ui.common.FinappFAB
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finapp.R
import com.example.finapp.home.common.HomeTopBar
import com.example.finapp.ui.common.FinappFAB
import com.example.finapp.ui.common.FinappListItem
import com.example.finapp.ui.theme.GreenPrimary

@Composable
fun IncomeRoute(
    viewModel: IncomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.value
    IncomeScreen(state = state, modifier = modifier)
}

@Composable
fun IncomeScreen(
    state: IncomeScreenUiState,
    modifier: Modifier = Modifier
) {
    IncomeContent(
        state = state,
        modifier = modifier
    )
}

@Composable
fun IncomeContent(
    state: IncomeScreenUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text("Доходы сегодня") },
                actions = {
                    IconButton(onClick = { }) {
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
                trailingContent = { Text(state.summary.totalFormatted) },
                green = true,
                height = 56
            )

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        title = item.title,
                        trailingContent = { Text(item.amountFormatted) },
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