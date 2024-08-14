package com.recipes.presentation.features.recipe_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipes.domain.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository
) : ViewModel() {
    companion object {
        const val STATE_KEY = "state"
    }

    private var stateValue: RecipeDetailsScreenState
        set(value) {
            savedStateHandle[STATE_KEY] = value
        }
        get() {
            return savedStateHandle.get<RecipeDetailsScreenState>(STATE_KEY)!!
        }
    val state = savedStateHandle.getStateFlow(STATE_KEY, RecipeDetailsScreenState())
    private val recipeId: MutableStateFlow<Int?> = MutableStateFlow(null)

    init {
        recipeId
            .filterNotNull()
            .flatMapLatest { id ->
                recipesRepository
                    .getRecipeFlow(id)
            }
            .filterNotNull()
            .onEach { recipe ->
                stateValue = stateValue.copy(
                    recipe = recipe
                )
            }
            .launchIn(viewModelScope)
    }

    fun saveRecipeId(rId: Int) {
        viewModelScope.launch {
            recipeId.emit(rId)
        }
    }

    fun removeRecipe(rId: Int) {
        recipesRepository.removeRecipe(rId)
    }
}