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
import com.finapp.core.settings.api.model.LanguageOption
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
    onOpenLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    SettingsScreen(
        state = state,
        onDarkThemeToggle = viewModel::onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onOpenAbout = onOpenAbout,
        onOpenPasscode = onOpenPasscode,
        onOpenLanguage = onOpenLanguage,
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
    onOpenLanguage: () -> Unit,
    onSelectThemeMode: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsContent(
        state = state,
        onDarkThemeToggle = onDarkThemeToggle,
        onOpenColorPicker = onOpenColorPicker,
        onOpenPasscode = onOpenPasscode,
        onOpenLanguage = onOpenLanguage,
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
    onOpenLanguage: () -> Unit,
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
                headlineContent = { Text(stringResource(R.string.settings_dark_theme)) },
                firstTrailingContent = {
                    Switch(
                        checked = state.themeMode == ThemeMode.DARK,
                        onCheckedChange = onDarkThemeToggle
                    )
                },
                height = 56
            )

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.settings_brand_color)) },
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
                R.string.settings_sounds,
                R.string.settings_haptics
            ).forEach { labelRes ->
                FinappListItem(
                    headlineContent = { Text(stringResource(labelRes)) },
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
                headlineContent = { Text(stringResource(R.string.settings_passcode)) },
                subtitle = stringResource(
                    if (state.passcodeIsSet) R.string.settings_passcode_set
                    else R.string.settings_passcode_unset
                ),
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

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.settings_sync)) },
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

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.settings_language)) },
                subtitle = state.language.nativeName(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_2),
                        contentDescription = null
                    )
                },
                clickable = true,
                onClick = onOpenLanguage,
                height = 72
            )

            FinappListItem(
                headlineContent = { Text(stringResource(R.string.settings_about)) },
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

private fun LanguageOption.nativeName(): String = when (this) {
    LanguageOption.RU -> "Русский"
    LanguageOption.EN -> "English"
}
