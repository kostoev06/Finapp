package com.finapp.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.finapp.core.settings.api.model.ThemeMode
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(),
    onOpenColorPicker: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    SettingsScreen(
        state = state,
        onDarkThemeToggle = viewModel::onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onOpenAbout = onOpenAbout,
        onSelectThemeMode = viewModel::onThemeModeSelect,
        modifier = modifier
    )
}

@Composable
fun SettingsScreen(
    state: SettingsScreenUiState,
    onDarkThemeToggle: (Boolean) -> Unit,
    onOpenColorPicker: () -> Unit,
    onOpenAbout: () -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsContent(
        state = state,
        onDarkThemeToggle = onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onSelectThemeMode = onSelectThemeMode,
        onOpenAbout = onOpenAbout,
        modifier = modifier
    )
}

@Composable
fun SettingsContent(
    state: SettingsScreenUiState,
    onDarkThemeToggle: (Boolean) -> Unit,
    onOpenColorPicker: () -> Unit,
    onOpenAbout: () -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.settings_top_title)) },
                actions = {}
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            FinappListItem(
                headlineContent = { Text("Тёмная тема") },
                firstTrailingContent = {
                    Switch(
                        checked = state.themeMode == ThemeMode.DARK,
                        onCheckedChange = onDarkThemeToggle
                    )
                },
                height = 56
            )

            FinappListItem(
                headlineContent = { Text("Основной цвет") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_2),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = onOpenColorPicker,
                height = 56
            )

            listOf(
                "Звуки",
                "Хаптики",
                "Код пароль",
                "Синхронизация",
                "Язык"
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
                    onClick = { /* TODO */ },
                    height = 56
                )
            }

            FinappListItem(
                headlineContent = { Text("О программе") },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_2),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = onOpenAbout,
                height = 56
            )
        }
    }
}
