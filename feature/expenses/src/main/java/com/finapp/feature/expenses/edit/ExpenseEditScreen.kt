package com.finapp.feature.expenses.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ContentAlpha
import com.finapp.feature.common.component.FinappDatePicker
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTimePicker
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.theme.GreenPrimaryLight
import com.finapp.feature.expenses.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseEditRoute(
    popBack: () -> Unit,
    viewModel: ExpenseEditViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var saveButtonEnabled by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExpenseEditUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = "${event.title}: ${event.message}"
                    )
                }

                ExpenseEditUiEvent.OnSaveSuccess -> {
                    popBack()
                    saveButtonEnabled = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        ExpenseEditScreen(
            state = state,
            onCategoryClick = viewModel::onCategoryClick,
            onCancelDialog = viewModel::onCancelDialog,
            onCategorySelect = viewModel::onCategorySelect,
            onAmountChange = viewModel::onAmountChange,
            onChooseDate = viewModel::onChooseDate,
            onChooseTime = viewModel::onChooseTime,
            onCommentChange = viewModel::onCommentChange,
            onSave = viewModel::onSave,
            saveButtonEnabled = saveButtonEnabled,
            popBack = popBack,
            modifier = modifier.padding(it)
        )
    }

}

@Composable
fun ExpenseEditScreen(
    state: ExpenseEditScreenUiState,
    onCategoryClick: () -> Unit,
    onCancelDialog: () -> Unit,
    onCategorySelect: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    onChooseDate: (LocalDate) -> Unit,
    onChooseTime: (LocalTime) -> Unit,
    onCommentChange: (String) -> Unit,
    onSave: () -> Unit,
    saveButtonEnabled: Boolean,
    popBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    FinappDatePicker(
        isShown = showDatePicker,
        onDismissRequest = { showDatePicker = false },
        onDateConfirm = {
            onChooseDate(it)
            showDatePicker = false
        },
        initialDate = state.dateState
    )

    FinappTimePicker(
        isShown = showTimePicker,
        initialTime = state.timeState,
        onConfirm = { newTime ->
            onChooseTime(newTime)
            showTimePicker = false
        },
        onDismissRequest = {
            showTimePicker = false
        }
    )

    ExpenseEditContent(
        state = state,
        onCategoryClick = onCategoryClick,
        onCancelDialog = onCancelDialog,
        onCategorySelect = onCategorySelect,
        onAmountChange = onAmountChange,
        onClickDate = { showDatePicker = true },
        onClickTime = { showTimePicker = true },
        onCommentChange = onCommentChange,
        onSave = onSave,
        saveButtonEnabled = saveButtonEnabled,
        popBack = popBack,
        modifier = modifier,
    )
}

@Composable
fun ExpenseEditContent(
    state: ExpenseEditScreenUiState,
    onCategoryClick: () -> Unit,
    onCancelDialog: () -> Unit,
    onCategorySelect: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    onClickDate: () -> Unit,
    onClickTime: () -> Unit,
    onCommentChange: (String) -> Unit,
    onSave: () -> Unit,
    saveButtonEnabled: Boolean,
    popBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backButtonEnabled by remember { mutableStateOf(true) }

    if (state.isCategoryDialogVisible) {
        AlertDialog(
            onDismissRequest = onCancelDialog,
            title = { Text("Выберите статью") },
            text = {
                Column {
                    state.categoriesListState.forEach { category ->
                        Text(
                            text = category.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelect(category.id)
                                    onCancelDialog()
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onCancelDialog) {
                    Text("Отмена")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }

    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.my_expenses)) },
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
        Column(Modifier.padding(inner)) {
            FinappListItem(
                headlineContent = { Text("Счет") },
                firstTrailingContent = {
                    Text(state.accountFieldState)
                }
            )
            FinappListItem(
                headlineContent = { Text("Статья") },
                firstTrailingContent = {
                    Text(state.currentCategoryState.name)
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right_1),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = onCategoryClick
            )
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.amount)) },
                firstTrailingContent = {
                    TextField(
                        value = state.amountFieldState,
                        onValueChange = { new ->
                            if (new.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                                onAmountChange(new)
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
                }
            )
            FinappListItem(
                headlineContent = { Text("Дата") },
                firstTrailingContent = { Text(state.dateState.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) },
                clickable = true,
                onClick = onClickDate,
            )
            FinappListItem(
                headlineContent = { Text("Время") },
                firstTrailingContent = { Text(state.timeState.format(DateTimeFormatter.ofPattern("HH:mm"))) },
                clickable = true,
                onClick = onClickTime,
            )
            FinappListItem(
                headlineContent = {
                    BasicTextField(
                        value = state.commentFieldState,
                        onValueChange = onCommentChange,
                        singleLine = true,
                        cursorBrush = SolidColor(GreenPrimary),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Start
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp)
                            ) {
                                if (state.commentFieldState.isEmpty()) {
                                    Text(
                                        "Комментарий",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            )
        }
    }
}
