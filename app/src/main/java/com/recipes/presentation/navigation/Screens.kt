package com.recipes.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Recipes : Screen()

    @Serializable
    @Parcelize
    data class RecipeDetails(val recipeId: Int) : Screen(), Parcelable

    @Serializable
    data object AddRecipe : Screen()
}