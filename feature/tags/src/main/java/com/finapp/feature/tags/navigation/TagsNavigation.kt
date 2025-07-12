package com.finapp.feature.tags.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.tags.TagsRoute
import com.finapp.feature.tags.di.LocalFeatureTagsComponentBuilder

inline fun <reified T : Any> NavGraphBuilder.tagsNavigation(
    navController: NavController
) {
    composable<T> {
        val tagsComponentBuilder = LocalFeatureTagsComponentBuilder.current
        val tagsComponent = remember { tagsComponentBuilder.build() }
        TagsRoute(
            viewModel = viewModel(factory = tagsComponent.viewModelFactory())
        )
    }
}