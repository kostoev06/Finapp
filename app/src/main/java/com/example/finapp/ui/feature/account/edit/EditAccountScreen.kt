package com.example.finapp.ui.feature.account.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.R
import com.example.finapp.ui.common.FinappListItem
import com.example.finapp.ui.common.HomeTopBar
import com.example.finapp.ui.feature.account.edit.viewmodel.EditAccountUiEvent
import com.example.finapp.ui.feature.account.edit.viewmodel.EditAccountViewModel
import com.example.finapp.ui.feature.account.edit.viewmodel.EditAccountViewModelFactory
import com.example.finapp.ui.theme.ErrorRed
import com.example.finapp.ui.utils.currencySymbol

@Composable
fun EditAccountRoute(
    viewModel: EditAccountViewModel = viewModel(factory = EditAccountViewModelFactory()),
    popBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditAccountUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = "${event.title}: ${event.message}"
                    )
                }

                EditAccountUiEvent.OnSaveSuccess -> { popBack() }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        EditAccountScreen(
            state = state,
            onNameChange = viewModel::onNameChange,
            onBalanceChange = viewModel::onBalanceChange,
            onCurrencyClick = viewModel::onCurrencyClick,
            onCurrencySelect = viewModel::onCurrencySelect,
            onCancelSheet = viewModel::onCancelSheet,
            popBack = popBack,
            onSave = viewModel::onSave,
            modifier = modifier.padding(it)
        )
    }

}

@Composable
fun EditAccountScreen(
    state: EditAccountScreenUiState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    onCurrencySelect: (String) -> Unit,
    onCancelSheet: () -> Unit,
    popBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    EditAccountContent(
        state = state,
        onNameChange = onNameChange,
        onBalanceChange = onBalanceChange,
        onCurrencyClick = onCurrencyClick,
        onCurrencySelect = onCurrencySelect,
        onCancelSheet = onCancelSheet,
        popBack = popBack,
        onSave = onSave,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountContent(
    state: EditAccountScreenUiState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    onCurrencySelect: (String) -> Unit,
    onCancelSheet: () -> Unit,
    popBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isCurrencySheetVisible) {
        ModalBottomSheet(onDismissRequest = onCancelSheet) {
            listOf(
                R.drawable.ic_rub to "RUB",
                R.drawable.ic_usd to "USD",
                R.drawable.ic_eur to "EUR"
            ).forEach { (iconRes, currency) ->
                ListItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null
                        )
                    },
                    headlineContent = {
                        Text(
                            when (currency) {
                                "RUB" -> "Российский рубль ₽"
                                "USD" -> "Американский доллар \$"
                                "EUR" -> "Евро €"
                                else -> currency
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCurrencySelect(currency) }
                        .height(72.dp)
                )
                HorizontalDivider()
            }
            ListItem(
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                headlineContent = {
                    Text(
                        text = "Отмена",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCancelSheet() }
                    .height(72.dp),
                colors = ListItemDefaults.colors(
                    containerColor = ErrorRed
                )
            )
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text(stringResource(R.string.account_top_title)) },
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(painterResource(R.drawable.ic_close), contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(painterResource(R.drawable.ic_check), contentDescription = null)
                    }
                }
            )
        },
        modifier = modifier
    ) { inner ->
        Column(modifier = Modifier.padding(inner)) {
            FinappListItem(
                title = "Название",
                firstTrailingContent = {
                    TextField(
                        value = state.nameFieldState.text,
                        onValueChange = onNameChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.End
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .widthIn(min = 140.dp, max = 240.dp)
                    )
                },
                clickable = false
            )

            FinappListItem(
                title = "Баланс",
                firstTrailingContent = {
                    TextField(
                        value = state.balanceFieldState.text,
                        onValueChange = { new ->
                            if (new.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                                onBalanceChange(new)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.End
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .widthIn(min = 140.dp, max = 240.dp)
                    )
                },
                clickable = false
            )

            FinappListItem(
                title = "Валюта",
                firstTrailingContent = { Text(currencySymbol(state.currencyFieldState.currency)) },
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_arrow_right_1),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = onCurrencyClick
            )
        }
    }
}