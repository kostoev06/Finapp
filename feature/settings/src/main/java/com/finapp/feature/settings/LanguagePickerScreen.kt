package com.finapp.feature.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.core.settings.api.model.LanguageOption
import com.finapp.feature.common.component.FinappTopAppBar

@Composable
fun LanguagePickerRoute(
    viewModel: SettingsViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LanguagePickerScreen(
        current = state.language,
        onPick = { option ->
            viewModel.onLanguageSelect(option)
            onBack()
        },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun LanguagePickerScreen(
    current: LanguageOption,
    onPick: (LanguageOption) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.settings_language)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { inner ->
        Column(Modifier.padding(inner).padding(vertical = 6.dp)) {
            LanguageOption.entries.forEach { option ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onPick(option) },
                    border = if (option == current) BorderStroke(2.dp, Color.Black) else null
                ) {
                    Row(Modifier.padding(16.dp)) {
                        Text(text = option.displayName())
                    }
                }
            }
        }
    }
}

/** Родное название языка — отображается одинаково независимо от текущей локали. */
private fun LanguageOption.displayName(): String = when (this) {
    LanguageOption.RU -> "Русский"
    LanguageOption.EN -> "English"
}
