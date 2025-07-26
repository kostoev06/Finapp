package com.finapp.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.common.theme.brandPalette

@Composable
fun BrandColorPickerRoute(
    viewModel: SettingsViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    BrandColorPickerScreen(
        current = state.brandColor,
        onPick = {
            viewModel.onBrandColorSelect(it)
            onBack()
        },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun BrandColorPickerScreen(
    current: BrandColorOption,
    onPick: (BrandColorOption) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backButtonEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { FinappTopAppBar(
            title = { Text("Основной цвет") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                        backButtonEnabled = false
                    },
                    enabled = backButtonEnabled
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }
        ) },
        modifier = modifier
    ) { inner ->
        Column(Modifier.padding(inner).padding(vertical = 6.dp)) {
            BrandColorOption.entries.forEach { opt ->
                val p = brandPalette(opt)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onPick(opt) },
                    border = if (opt == current) BorderStroke(2.dp, Color.Black) else null
                ) {
                    Row(Modifier.padding(12.dp)) {
                        Box(
                            Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(p.primary)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(opt.name)
                    }
                }
            }
        }
    }
}