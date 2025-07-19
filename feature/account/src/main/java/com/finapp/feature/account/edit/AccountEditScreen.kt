package com.finapp.feature.account.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.feature.account.R
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.theme.ErrorRed
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.theme.GreenPrimaryLight
import com.finapp.feature.common.utils.currencySymbolRes

@Composable
fun AccountEditRoute(
    viewModel: AccountEditViewModel = viewModel(),
    popBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsState().value
    var saveButtonEnabled by remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AccountEditUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = "${event.title}: ${event.message}"
                    )
                }

                AccountEditUiEvent.OnSaveSuccess -> {
                    popBack()
                    saveButtonEnabled = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        AccountEditScreen(
            state = state,
            onNameChange = viewModel::onNameChange,
            onBalanceChange = viewModel::onBalanceChange,
            onCurrencyClick = viewModel::onCurrencyClick,
            onCurrencySelect = viewModel::onCurrencySelect,
            onCancelSheet = viewModel::onCancelSheet,
            popBack = popBack,
            saveButtonEnabled = saveButtonEnabled,
            onSave = viewModel::onSave,
            modifier = modifier.padding(it)
        )
    }

}

@Composable
fun AccountEditScreen(
    state: AccountEditScreenUiState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    onCurrencySelect: (CurrencyCode) -> Unit,
    onCancelSheet: () -> Unit,
    popBack: () -> Unit,
    saveButtonEnabled: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    AccountEditContent(
        state = state,
        onNameChange = onNameChange,
        onBalanceChange = onBalanceChange,
        onCurrencyClick = onCurrencyClick,
        onCurrencySelect = onCurrencySelect,
        onCancelSheet = onCancelSheet,
        popBack = popBack,
        saveButtonEnabled = saveButtonEnabled,
        onSave = onSave,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditContent(
    state: AccountEditScreenUiState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    onCurrencySelect: (CurrencyCode) -> Unit,
    onCancelSheet: () -> Unit,
    popBack: () -> Unit,
    saveButtonEnabled: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backButtonEnabled by remember { mutableStateOf(true) }

    if (state.isCurrencySheetVisible) {
        ModalBottomSheet(onDismissRequest = onCancelSheet) {
            listOf(
                R.drawable.ic_rub to CurrencyCode.RUB,
                R.drawable.ic_usd to CurrencyCode.USD,
                R.drawable.ic_eur to CurrencyCode.EUR
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
                                CurrencyCode.RUB -> stringResource(R.string.currency_name_rub)
                                CurrencyCode.USD -> stringResource(R.string.currency_name_usd)
                                CurrencyCode.EUR -> stringResource(R.string.currency_name_eur)
                                else -> currency.code
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
                        text = stringResource(R.string.cancel),
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
            FinappTopAppBar(
                title = { Text(stringResource(R.string.account_top_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            popBack()
                            backButtonEnabled = false
                        },
                        enabled = backButtonEnabled
                    ) {
                        Icon(painterResource(R.drawable.ic_close), contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSave,
                        enabled = saveButtonEnabled
                    ) {
                        Icon(painterResource(R.drawable.ic_check), contentDescription = null)
                    }
                }
            )
        },
        modifier = modifier
    ) { inner ->
        Column(modifier = Modifier.padding(inner)) {
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.title)) },
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
                            cursorColor = GreenPrimary,
                            selectionColors = TextSelectionColors(GreenPrimary, GreenPrimaryLight)
                        ),
                        modifier = Modifier
                            .widthIn(min = 140.dp, max = 240.dp)
                    )
                },
                clickable = false
            )

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.balance)) },
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
                            cursorColor = GreenPrimary,
                            selectionColors = TextSelectionColors(GreenPrimary, GreenPrimaryLight)
                        ),
                        modifier = Modifier
                            .widthIn(min = 140.dp, max = 240.dp)
                    )
                },
                clickable = false
            )

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.currency)) },
                firstTrailingContent = {
                    Text(
                        stringResource(
                            currencySymbolRes(state.currencyFieldState.currency)
                        )
                    )
                },
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