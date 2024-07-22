package com.recipes.presentation.features.recipes

sealed class RecipesScreenActions {
    data class OnNewSearchText(val searchText: String) : RecipesScreenActions()
    data class OnNewFilterPinned(val filterOnlyPinned: Boolean) : RecipesScreenActions()
    data class OnRemoveRecipeFromPinned(val recipeId: Int) : RecipesScreenActions()
    data class OnPinRecipe(val recipeId: Int) : RecipesScreenActions()
}
