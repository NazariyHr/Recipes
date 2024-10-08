package com.recipes.domain.repository

import com.recipes.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getRecipesFlow(): Flow<List<Recipe>>
    fun getRecipeFlow(recipeId: Int): Flow<Recipe?>
    fun getFoldersFlow(): Flow<List<String>>
    fun addNewRecipe(recipe: Recipe)
    fun changeRecipeIsPinned(recipeId: Int, isPinned: Boolean)
    fun getNextId(): Int
    fun removeRecipe(recipeId: Int)
}