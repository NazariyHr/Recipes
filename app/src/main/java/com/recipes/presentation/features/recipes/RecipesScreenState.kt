package com.recipes.presentation.features.recipes

import android.os.Parcelable
import com.recipes.domain.model.Recipe
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipesScreenState(
    val recipes: List<Recipe> = emptyList(),
    val folders: List<String> = listOf()
) : Parcelable
