package com.recipes.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int,
    val photoUriStr: String?,
    val title: String,
    val description: String,
    val folder: String,
    val ingredients: String,
    val steps: String,
    val tags: String,
    val isFavorite: Boolean
) : Parcelable
