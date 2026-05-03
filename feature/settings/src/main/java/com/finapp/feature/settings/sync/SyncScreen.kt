package com.finapp.feature.settings.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.settings.R
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SyncRoute(
    viewModel: SyncViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val syncStartedMessage = stringResource(R.string.sync_started)

    SyncScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onSyncClick = {
            viewModel.onSyncClick()
            // Сам worker выполняется асинхронно WorkManager'ом (constraint NetworkType.CONNECTED) —
            // здесь только подтверждаем, что задача поставлена в очередь.
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(syncStartedMessage)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun SyncScreen(
    state: SyncScreenUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]
    val formatter = remember(locale) {
        DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", locale)
            .withZone(ZoneId.systemDefault())
    }

    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.sync_title)) },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { inner ->
        Column(modifier = Modifier.padding(inner)) {
            FinappListItem(
                headlineContent = { Text(stringResource(R.string.sync_last)) },
                firstTrailingContent = {
                    Text(
                        text = state.lastSyncEpochSeconds
                            ?.let { formatter.format(Instant.ofEpochSecond(it)) }
                            ?: stringResource(R.string.sync_never)
                    )
                },
                height = 56
            )

            Button(
                onClick = onSyncClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(stringResource(R.string.sync_button))
            }
        }
    }
}
