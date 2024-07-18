package com.recipes.presentation.features.recipe_details

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun RecipeDetailsScreenRoot(
    recipeId: Int,
    navController: NavController,
    viewModel: RecipeDetailsViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RecipeDetailsScreen(
        state = state
    )
}

@Composable
private fun RecipeDetailsScreen(
    state: RecipeDetailsScreenState
) {
    Box {
        Text(text = "Recipe Details")
    }
}

@Preview
@Composable
private fun RecipeDetailsScreenPreview() {
    RecipesTheme {
        RecipeDetailsScreen(
            state = RecipeDetailsScreenState(

            )
        )
    }
}