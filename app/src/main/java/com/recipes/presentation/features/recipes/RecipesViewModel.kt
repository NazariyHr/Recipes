package com.recipes.presentation.features.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.recipes.domain.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val filterOnlyFavorites: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {

    }
}