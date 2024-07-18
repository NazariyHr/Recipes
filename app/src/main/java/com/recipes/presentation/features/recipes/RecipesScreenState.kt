package com.recipes.presentation.features.recipes

import com.recipes.domain.model.Recipe

data class RecipesScreenState(
    val recipes: List<Recipe> = emptyList()
)
