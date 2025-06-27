package com.example.finapp.ui.home.settings.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.ui.home.settings.viewmodel.SettingsViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import com.example.finapp.R
import com.example.finapp.ui.home.common.HomeTopBar
import com.example.finapp.ui.common.FinappListItem

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
            HomeTopBar(
                title = { Text("Настройки") },
                actions = { }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinappListItem(
                title = "Тёмная тема",
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
                    title = label,
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
