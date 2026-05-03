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
import com.finapp.feature.settings.navigation.PasscodeNavMode

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel(),
    onOpenColorPicker: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenPasscode: (PasscodeNavMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    SettingsScreen(
        state = state,
        onDarkThemeToggle = viewModel::onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onOpenAbout = onOpenAbout,
        onOpenPasscode = onOpenPasscode,
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
    onOpenPasscode: (PasscodeNavMode) -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsContent(
        state = state,
        onDarkThemeToggle = onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onOpenPasscode = onOpenPasscode,
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
    onOpenPasscode: (PasscodeNavMode) -> Unit,
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
                "Хаптики"
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
                headlineContent = { Text("Код пароль") },
                subtitle = if (state.passcodeIsSet) "Установлен" else "Не установлен",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_2),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = {
                    onOpenPasscode(
                        if (state.passcodeIsSet) PasscodeNavMode.DISABLE
                        else PasscodeNavMode.SETUP_NEW
                    )
                },
                height = 72
            )

            listOf(
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
