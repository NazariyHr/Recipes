package com.recipes.presentation.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppTheme = lightColorScheme(
    primary = LightGreen,
    secondary = Gray,
    tertiary = LightGrey
)

@Composable
fun RecipesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppTheme,
        typography = Typography,
        content = content
    )
}