package com.recipes.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.recipes.domain.model.Recipe
import com.recipes.domain.repository.RecipesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipesRepositoryImpl(
    context: Context
) : RecipesRepository {

    companion object {
        private const val PREFERENCES_NAME = "prefs_recipes"

        private const val RECIPES = "recipes"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val recipesFlow = MutableSharedFlow<List<Recipe>>(replay = 1)

    init {
        updateRecipesFlow()
    }

    private fun updateRecipesFlow() {
        scope.launch {
            val newRecipes = getRecipesFromPrefs()
            recipesFlow.emit(newRecipes)
        }
    }

    private fun getRecipesFromPrefs(): List<Recipe> {
        val recipesStr = prefs.getString(RECIPES, "")
        if (recipesStr.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return Gson().fromJson(recipesStr, type)
    }

    override fun getRecipesFlow(): Flow<List<Recipe>> = recipesFlow.asSharedFlow()

    override fun addNewRecipe(recipe: Recipe) {
        val newRecipes = getRecipesFromPrefs() + recipe
        val newRecipesStr = Gson().toJson(newRecipes)
        prefs.edit { putString(RECIPES, newRecipesStr) }
        updateRecipesFlow()
    }

    override fun getRecipeFlow(recipeId: Int): Flow<Recipe?> =
        getRecipesFlow().map { recipes -> recipes.find { r -> r.id == recipeId } }

    override fun getFoldersFlow(): Flow<List<String>> =
        getRecipesFlow().map { recipes -> recipes.map { it.folder } }

    override fun changeRecipeIsPinned(recipeId: Int, isPinned: Boolean) {
        val newRecipes =
            getRecipesFromPrefs()
                .map { if (it.id == recipeId) it.copy(isPinned = isPinned) else it }
        val newRecipesStr = Gson().toJson(newRecipes)
        prefs.edit { putString(RECIPES, newRecipesStr) }
        updateRecipesFlow()
    }

    override fun getNextId(): Int = (getRecipesFromPrefs().lastOrNull()?.id ?: 0) + 1
}