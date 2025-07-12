package com.finapp.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.value
    SettingsScreen(
        state = state,
        onDarkThemeToggle = viewModel::onDarkThemeToggle,
        modifier = modifier
    )
}

@Composable
fun SettingsScreen(
    state: SettingsScreenUiState,
    onDarkThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsContent(
        state = state,
        onDarkThemeToggle = onDarkThemeToggle,
        modifier = modifier
    )
}

@Composable
fun SettingsContent(
    state: SettingsScreenUiState,
    onDarkThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.settings_top_title)) },
                actions = { }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                headlineContent = { Text("Тёмная тема") },
                firstTrailingContent = {
                    Switch(
                        checked = state.isDarkThemeEnabled,
                        onCheckedChange = onDarkThemeToggle
                    )
                },
                height = 56
            )

            listOf(
                "Основной цвет",
                "Звуки",
                "Хаптики",
                "Код пароль",
                "Синхронизация",
                "Язык",
                "О программе"
            ).forEach { label ->
                FinappListItem(
                    headlineContent = { Text(label) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right_2),
                            contentDescription = null
                        )
                    },
                    clickable = true,
                    onClick = { },
                    height = 56
                )
            }
        }
    }
}
