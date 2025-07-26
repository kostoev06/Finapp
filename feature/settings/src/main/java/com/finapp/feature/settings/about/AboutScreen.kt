package com.finapp.feature.settings.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.feature.settings.di.LocalFeatureSettingsComponentBuilder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.component.FinappListItem
import com.finapp.feature.settings.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource


@Composable
fun AboutRoute(
    viewModel: AboutViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AboutScreen(
        state = viewModel.uiState,
        onBack = onBack,
        modifier = modifier
    )
}


@Composable
fun AboutScreen(
    state: AboutUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateText = rememberDate(state.lastUpdateMillis)

    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            FinappListItem(
                headlineContent = { Text("Версия", fontWeight = FontWeight.Medium) },
                firstTrailingContent = { Text(state.versionName) },
                height = 56
            )
            FinappListItem(
                headlineContent = { Text("Версия (code)") },
                firstTrailingContent = { Text(state.versionCode.toString()) },
                height = 56
            )
            FinappListItem(
                headlineContent = { Text("Последнее обновление") },
                firstTrailingContent = { Text(dateText) },
                height = 56
            )
        }
    }
}

@Composable
private fun rememberDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    val zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
    return formatter.format(zdt)
}