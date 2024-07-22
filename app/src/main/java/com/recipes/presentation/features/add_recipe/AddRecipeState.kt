package com.recipes.presentation.features.add_recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddRecipeState(
    val t: String = "",
    val foldersList: List<String> = emptyList()
) : Parcelable
