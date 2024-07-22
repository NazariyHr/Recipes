package com.recipes.presentation.features.add_recipe

sealed class AddRecipeScreenActions {
    data class SaveRecipe(
        val photoUriStr: String?,
        val title: String,
        val description: String,
        val folder: String,
        val ingredients: String,
        val steps: String,
        val tags: String
    ) : AddRecipeScreenActions()
}
