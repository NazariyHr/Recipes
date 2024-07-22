package com.recipes.presentation.features.recipe_details

import android.os.Parcelable
import com.recipes.domain.model.Recipe
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeDetailsScreenState(
    val recipe: Recipe? = null
) : Parcelable
