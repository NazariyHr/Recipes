package com.recipes.presentation.features.recipe_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import com.recipes.R
import com.recipes.domain.model.Recipe
import com.recipes.presentation.common.modifiers.drawNoteSheetOnBackGround
import com.recipes.presentation.common.theme.CianBlueDarker
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.FontEduAuvicWantHandBold
import com.recipes.presentation.common.theme.FontEduAuvicWantHandMedium
import com.recipes.presentation.common.theme.FontEduAuvicWantHandRegular
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.MainBgColor
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.common.utils.formatCookingSteps
import com.recipes.presentation.common.utils.formatIngredients
import com.recipes.presentation.common.utils.formatTags
import com.recipes.presentation.features.add_recipe.components.Pin
import com.recipes.presentation.features.add_recipe.components.RemoveButton

@Composable
fun RecipeDetailsScreenRoot(
    recipeId: Int,
    navController: NavController,
    viewModel: RecipeDetailsViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.saveRecipeId(recipeId)
    RecipeDetailsScreen(
        state = state,
        onCloseClicked = {
            navController.navigateUp()
        },
        onRemoveClicked = {
            viewModel.removeRecipe(recipeId)
            navController.navigateUp()
        }
    )
}

@Composable
private fun RecipeDetailsScreen(
    onCloseClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    state: RecipeDetailsScreenState
) {
    val recipe = state.recipe ?: return

    val context = LocalContext.current
    val d = LocalDensity.current
    val scrollState = rememberScrollState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MainBgColor)
                .padding(paddingValues)
        ) {
            // Screen title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    fontFamily = FontEduAuvicWantHandBold,
                    text = "Recipe details",
                    modifier = Modifier
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = DarkGrey
                )
                RemoveButton(
                    onClicked = {
                        onCloseClicked()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp, top = 12.dp)
                )
            }

            // Note content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .drawNoteSheetOnBackGround()
                    .padding(start = 30.dp, end = 8.dp, bottom = 8.dp)
            ) {
                var imageWidth by remember {
                    mutableStateOf(0.dp)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPlaced {
                            imageWidth = with(d) { (it.size.width.toFloat() * 0.8f).toDp() }
                        }

                ) {
                    // Photo
                    if (!recipe.photoUriStr.isNullOrEmpty()) {
                        Box {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .data(recipe.photoUriStr)
                                    .size(
                                        Size(
                                            with(LocalDensity.current) {
                                                162.dp.toPx().toInt()
                                            },
                                            Dimension.Undefined
                                        )
                                    )
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(162.dp)
                                    .width(imageWidth)
                                    .rotate(-3f)
                                    .padding(top = 12.dp)
                            )
                            Pin(
                                modifier = Modifier
                                    .size(width = 30.dp, height = 70.dp)
                                    .rotate(3f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 18.dp)
                            .verticalScroll(scrollState)
                    ) {
                        // Title
                        Row {
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "title:",
                                modifier = Modifier
                                    .align(Alignment.Bottom)
                                    .padding(end = 4.dp),
                                textAlign = TextAlign.Start,
                                fontSize = 18.sp,
                                color = DarkGrey
                            )
                            Text(
                                text = recipe.title,
                                modifier = Modifier
                                    .widthIn(min = imageWidth / 1.4f),
                                textAlign = TextAlign.Start,
                                style = TextStyle(
                                    fontFamily = FontEduAuvicWantHandMedium,
                                    fontSize = 20.sp,
                                    color = DarkGrey
                                )
                            )
                        }

                        // Description
                        Text(
                            text = recipe.description,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                fontFamily = FontEduAuvicWantHandMedium,
                                fontSize = 16.sp,
                                color = DarkGrey
                            )
                        )

                        // Folder field
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_folder),
                                colorFilter = ColorFilter.tint(DarkGrey),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(32.dp)
                                    .align(Alignment.CenterVertically)
                            )

                            Text(
                                text = recipe.folder,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .widthIn(min = imageWidth / 1.4f),
                                textAlign = TextAlign.Start,
                                style = TextStyle(
                                    fontFamily = FontEduAuvicWantHandMedium,
                                    fontSize = 16.sp,
                                    color = DarkGrey
                                )
                            )
                        }

                        // Tags
                        Text(
                            fontFamily = FontEduAuvicWantHandMedium,
                            text = recipe.tags.formatTags(),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            color = CianBlueDarker
                        )

                        // Ingredients
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "ingredients:",
                                modifier = Modifier,
                                textAlign = TextAlign.Start,
                                fontSize = 14.sp,
                                color = DarkGrey
                            )
                            Text(
                                fontFamily = FontEduAuvicWantHandMedium,
                                text = recipe.ingredients.formatIngredients(),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = DarkGrey
                            )
                        }

                        // Cooking steps
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                fontFamily = FontEduAuvicWantHandRegular,
                                text = "Cooking steps",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = DarkGrey
                            )
                            Text(
                                fontFamily = FontEduAuvicWantHandMedium,
                                text = recipe.steps.formatCookingSteps(),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = DarkGrey
                            )
                        }
                    }


                }
                Image(
                    painter = painterResource(id = R.drawable.ic_remove_recipe),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 4.dp, top = 8.dp)
                        .rotate(-3f)
                        .align(Alignment.TopEnd)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(
                                bounded = false,
                                radius = 32.dp,
                                color = Color.Red
                            )
                        ) {
                            onRemoveClicked()
                        }
                        .size(32.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecipeDetailsScreenPreview() {
    val i = 1
    RecipesTheme {
        RecipeDetailsScreen(
            state = RecipeDetailsScreenState(
                recipe = Recipe(
                    i,
                    null,
                    "title $i",
                    "description $i",
                    "folder $i",
                    "ingredients $i",
                    "steps $i",
                    "tags $i",
                    true
                )
            ),
            onCloseClicked = {},
            onRemoveClicked = {}
        )
    }
}