package com.finapp.feature.settings.sound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.sound.LocalSoundPlayer
import com.finapp.feature.common.sound.SoundEffect
import com.finapp.feature.settings.R

@Composable
fun SoundRoute(
    viewModel: SoundViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val soundPlayer = LocalSoundPlayer.current

    SoundScreen(
        state = state,
        onBack = onBack,
        onToggle = viewModel::onToggle,
        // Тестовые кнопки игнорируют свитч — пользователь должен мочь послушать звук
        // даже когда он выключен, чтобы решить включать или нет.
        onTestSuccess = { soundPlayer.play(SoundEffect.Success, ignoreSetting = true) },
        onTestError = { soundPlayer.play(SoundEffect.Error, ignoreSetting = true) },
        modifier = modifier
    )
}

@Composable
private fun SoundScreen(
    state: SoundScreenUiState,
    onBack: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onTestSuccess: () -> Unit,
    onTestError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.sound_title)) },
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
        Column(modifier = Modifier.padding(inner)) {
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.sound_enabled_label)) },
                firstTrailingContent = {
                    Switch(checked = state.enabled, onCheckedChange = onToggle)
                },
                height = 56
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onTestSuccess,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.sound_test_success))
                }
                OutlinedButton(
                    onClick = onTestError,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.sound_test_error))
                }
            }
        }
    }
}
