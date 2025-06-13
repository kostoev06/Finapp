package com.example.finapp.home.tags.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finapp.home.tags.viewmodel.TagsViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finapp.R
import com.example.finapp.home.common.HomeTopBar
import com.example.finapp.ui.common.FinappListItem

@Composable
fun TagsRoute(
    viewModel: TagsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.value
    TagsScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        modifier = modifier
    )
}

@Composable
fun TagsScreen(
    state: TagsScreenUiState,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TagsContent(state = state, onQueryChange = onQueryChange, modifier = modifier)
}

@Composable
fun TagsContent(
    state: TagsScreenUiState,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                title = { Text("Мои статьи") }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TagsSearchField(
                searchState = state.search,
                onQueryChange = onQueryChange
            )

            HorizontalDivider()

            LazyColumn {
                items(state.items) { item ->
                    FinappListItem(
                        leadingSymbols = item.leadingSymbols,
                        title = item.title,
                        onClick = { }
                    )
                }
            }
        }
    }
}


@Composable
fun TagsSearchField(
    searchState: SearchUiState,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchState.query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Найти статью") },
        trailingIcon = {
            Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun TagsSearchFieldPreview() {
    MaterialTheme {
        TagsSearchField(
            searchState = SearchUiState(query = "Продукты"),
            onQueryChange = {}
        )
    }
}