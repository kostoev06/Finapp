package com.finapp.feature.settings.passcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finapp.feature.common.component.FinappTopAppBar
import com.finapp.feature.settings.R
import com.finapp.feature.settings.passcode.component.PasscodeDots
import com.finapp.feature.settings.passcode.component.PasscodeKeypad

@Composable
fun PasscodeRoute(
    viewModel: PasscodeViewModel = viewModel(),
    onBack: () -> Unit,
    onDone: () -> Unit,
    showBack: Boolean = true,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.finished) {
        if (state.finished) onDone()
    }

    PasscodeScreen(
        state = state,
        onDigit = viewModel::onDigit,
        onBackspace = viewModel::onBackspace,
        onBack = onBack,
        showBack = showBack,
        modifier = modifier
    )
}

@Composable
fun PasscodeScreen(
    state: PasscodeUiState,
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    onBack: () -> Unit,
    showBack: Boolean,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FinappTopAppBar(
                title = { Text(stringResource(state.mode.topTitleRes())) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(state.mode.promptRes()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                PasscodeDots(total = PASSCODE_LENGTH, filled = state.entered.length)
                Box(modifier = Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                    state.errorRes?.let { res ->
                        Text(
                            text = stringResource(res),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            PasscodeKeypad(
                onDigit = onDigit,
                onBackspace = onBackspace
            )

            Spacer(Modifier.height(0.dp))
        }
    }
}

private fun PasscodeMode.topTitleRes(): Int = when (this) {
    PasscodeMode.SetupNew, PasscodeMode.SetupConfirm -> com.finapp.feature.settings.R.string.passcode_title_setup
    PasscodeMode.Verify -> com.finapp.feature.settings.R.string.passcode_title_verify
    PasscodeMode.Disable -> com.finapp.feature.settings.R.string.passcode_title_disable
}

private fun PasscodeMode.promptRes(): Int = when (this) {
    PasscodeMode.SetupNew -> com.finapp.feature.settings.R.string.passcode_prompt_setup_new
    PasscodeMode.SetupConfirm -> com.finapp.feature.settings.R.string.passcode_prompt_setup_confirm
    PasscodeMode.Verify -> com.finapp.feature.settings.R.string.passcode_prompt_verify
    PasscodeMode.Disable -> com.finapp.feature.settings.R.string.passcode_prompt_disable
}
