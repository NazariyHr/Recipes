package com.recipes.domain.model

data class Recipe(
    val id: Int,
    val photoFileName: String,
    val title: String,
    val description: String,
    val category: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>
)
