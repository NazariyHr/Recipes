package com.recipes.presentation.features.add_recipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipes.domain.model.Recipe
import com.recipes.domain.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository
) : ViewModel() {
    companion object {
        const val STATE_KEY = "state"
    }

    private var stateValue: AddRecipeState
        set(value) {
            savedStateHandle[STATE_KEY] = value
        }
        get() {
            return savedStateHandle.get<AddRecipeState>(STATE_KEY)!!
        }
    val state = savedStateHandle.getStateFlow(STATE_KEY, AddRecipeState())


    init {
        recipesRepository
            .getFoldersFlow()
            .onEach { folders ->
                stateValue = stateValue.copy(
                    foldersList = folders
                )
            }
            .launchIn(viewModelScope)

//        viewModelScope.launch {
//            val foldersList = mutableListOf<String>()
//            for (i in 1..1000) {
//                foldersList.add("folder $i")
//            }
//            stateValue = stateValue.copy(foldersList = foldersList)
//        }
    }

    fun onAction(action: AddRecipeScreenActions) {
        when (action) {
            is AddRecipeScreenActions.SaveRecipe -> {
                recipesRepository.addNewRecipe(
                    Recipe(
                        recipesRepository.getNextId(),
                        action.photoUriStr,
                        action.title,
                        action.description,
                        action.folder,
                        action.ingredients,
                        action.steps,
                        action.tags,
                        false
                    )
                )
            }
        }
    }
}