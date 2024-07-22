package com.recipes.presentation.features.add_recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddRecipeState(
    val foldersList: List<String> = emptyList(),
    val permissionsWasAsked: Boolean = false
) : Parcelable
