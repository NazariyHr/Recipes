package com.recipes.presentation.common.utils

fun String.formatTags(): String {
    val tags = this
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map {
            "#" + it
                .replace(" ", "_")
                .replace("\n", "_")
        }
        .distinct()
    return tags.joinToString(separator = "  ")
}

fun String.formatIngredients(): String {
    val ingredients = this
        .split(",", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map {
            "-  $it"
        }
    val formattedIngredients =
        ingredients.joinToString(separator = "\n")
    return formattedIngredients
}

fun String.formatCookingSteps(): String {
    val steps = this
        .split("\n\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .mapIndexed { index, step ->
            "${index + 1}.  $step"
        }
    val formattedSteps =
        steps.joinToString(separator = "\n\n")
    return formattedSteps
}