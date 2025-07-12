package com.finapp.feature.common.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.finapp.feature.common.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinappTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = GreenPrimary
        ),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier
    )

}
