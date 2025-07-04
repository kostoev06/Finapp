package com.example.finapp.ui.feature.account

import com.example.finapp.ui.feature.account.viewmodel.AccountViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.finapp.R
import com.example.finapp.ui.common.HomeTopBar
import com.example.finapp.ui.common.FinappListItem
import com.example.finapp.ui.feature.account.edit.viewmodel.EditAccountUiEvent
import com.example.finapp.ui.feature.account.viewmodel.AccountUiEvent
import com.example.finapp.ui.feature.account.viewmodel.AccountViewModelFactory
import com.example.finapp.ui.utils.currencySymbol


@Composable
fun AccountRoute(
    viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory()),
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
            HomeTopBar(
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
                title = "Баланс",
                firstTrailingContent = {
                    Text(
                        stringResource(
                            R.string.amount_with_currency,
                            state.balanceState.balance,
                            currencySymbol(state.currencyState.currency)
                        )
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right_1),
                        contentDescription = null
                    )
                },
                green = true,
                height = 56,
                onClick = { }
            )
            FinappListItem(
                title = "Валюта",
                firstTrailingContent = { Text(currencySymbol(state.currencyState.currency)) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right_1),
                        contentDescription = null
                    )
                },
                green = true,
                height = 56,
                onClick = { }
            )
        }
    }
}
