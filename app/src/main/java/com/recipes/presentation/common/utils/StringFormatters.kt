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