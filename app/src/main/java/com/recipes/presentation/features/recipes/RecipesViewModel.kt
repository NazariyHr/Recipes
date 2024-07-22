package com.recipes.presentation.features.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipes.domain.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository
) : ViewModel() {
    companion object {
        const val STATE_KEY = "state"
    }

    private var stateValue: RecipesScreenState
        set(value) {
            savedStateHandle[STATE_KEY] = value
        }
        get() {
            return savedStateHandle.get<RecipesScreenState>(STATE_KEY)!!
        }
    val state = savedStateHandle.getStateFlow(STATE_KEY, RecipesScreenState())

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private val selectedFolder: MutableStateFlow<String?> = MutableStateFlow(null)
    private val filterOnlyPinned: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        recipesRepository
            .getFoldersFlow()
            .onEach { folders ->
                stateValue = stateValue.copy(
                    folders = folders.distinct()
                )
            }
            .launchIn(viewModelScope)

        combine(
            recipesRepository.getRecipesFlow(),
            searchText,
            selectedFolder,
            filterOnlyPinned
        ) { recipes, searchText, selectedFolder, onlyFavorites ->
            recipes
                .filter { recipe ->
                    if (selectedFolder != null) recipe.folder == selectedFolder
                    else true
                }
                .filter { recipe ->
                    if (onlyFavorites) recipe.isPinned
                    else true
                }
                .filter { recipe ->
                    if (searchText.isNotEmpty()) {
                        recipe.title.contains(searchText) ||
                                recipe.folder.contains(searchText) ||
                                recipe.tags.contains(searchText)
                    } else {
                        true
                    }
                }
        }
            .onEach { filteredRecipes ->
                stateValue = stateValue.copy(
                    recipes = filteredRecipes
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RecipesScreenActions) {
        when (action) {
            is RecipesScreenActions.OnNewFilterPinned -> {
                viewModelScope.launch {
                    filterOnlyPinned.emit(action.filterOnlyPinned)
                }
            }

            is RecipesScreenActions.OnNewSearchText -> {
                viewModelScope.launch {
                    searchText.emit(action.searchText)
                }
            }

            is RecipesScreenActions.OnPinRecipe -> {
                recipesRepository.changeRecipeIsPinned(action.recipeId, true)
            }

            is RecipesScreenActions.OnRemoveRecipeFromPinned -> {
                recipesRepository.changeRecipeIsPinned(action.recipeId, false)
            }

            is RecipesScreenActions.OnNewFolderText ->{
                viewModelScope.launch {
                    selectedFolder.emit(action.folder)
                }
            }
        }
    }
}