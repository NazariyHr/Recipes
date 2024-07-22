package com.recipes.presentation.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.features.add_recipe.AddRecipeScreenRoot
import com.recipes.presentation.features.recipe_details.RecipeDetailsScreenRoot
import com.recipes.presentation.features.recipes.RecipesScreenRoot
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Composable
fun AppNavigationRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AppNavigation(
        navController,
        modifier
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Recipes,
            modifier = Modifier.weight(1f)
        ) {
            composableNoTransition<Screen.Recipes> {
                RecipesScreenRoot(navController)
            }
            composableNoTransition<Screen.RecipeDetails>(
                typeMap = mapOf(
                    typeOf<Screen.RecipeDetails>() to parcelableType<Screen.RecipeDetails>()
                )
            ) {
                val recipeId = it.toRoute<Screen.RecipeDetails>().recipeId
                RecipeDetailsScreenRoot(recipeId, navController)
            }
            composable<Screen.AddRecipe>(
                enterTransition = {
                    scaleIn(
                        transformOrigin = TransformOrigin(0.5f, 0.2f)
                    )
                },
                exitTransition = {
                    scaleOut(
                        animationSpec = tween(
                            easing = LinearEasing
                        ),
                        targetScale = 0f,
                        transformOrigin = TransformOrigin(0.5f, 0.2f)
                    )
                }
            ) {
                AddRecipeScreenRoot(navController)
            }
        }
    }
}

@Preview
@Composable
fun AppNavigationPreview(modifier: Modifier = Modifier) {
    RecipesTheme {
        AppNavigation(
            navController = rememberNavController()
        )
    }
}

inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

inline fun <reified T : Any> NavGraphBuilder.composableNoTransition(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        content = content
    )
}
