package com.example.finapp.home.account.view

import com.example.finapp.home.account.viewmodel.AccountViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finapp.R
import com.example.finapp.home.common.HomeTopBar
import com.example.finapp.ui.common.FinappFAB
import com.example.finapp.ui.common.FinappListItem


@Composable
fun AccountRoute(
    viewModel: AccountViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.value
    AccountScreen(state = state, modifier = modifier)
}

@Composable
fun AccountScreen(
    state: AccountScreenUiState,
    modifier: Modifier = Modifier
) {
    AccountContent(state = state, modifier = modifier)
}

@Composable
fun AccountContent(
    state: AccountScreenUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text("Мой счёт") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
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
                leadingSymbols = "💰",
                title = "Баланс",
                trailingContent = { Text(state.balance.totalFormatted) },
                trailingIcon = { Icon(painter = painterResource(R.drawable.ic_arrow_right_1), contentDescription = null) },
                green = true,
                clickable = true,
                height = 56,
                onClick = { }
            )
            FinappListItem(
                title = "Валюта",
                trailingContent = { Text(state.currency.currency) },
                trailingIcon = { Icon(painter = painterResource(R.drawable.ic_arrow_right_1), contentDescription = null) },
                green = true,
                clickable = true,
                height = 56,
                onClick = { }
            )
        }
    }
}