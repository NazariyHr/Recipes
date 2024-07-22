package com.recipes.presentation.features.recipes

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
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
import com.recipes.presentation.common.components.keyboardIsOpened
import com.recipes.presentation.common.modifiers.drawNoteSheetOnBackGround
import com.recipes.presentation.common.theme.CianBlueDarker
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.FontEduAuvicWantHandMedium
import com.recipes.presentation.common.theme.FontPlayWriteCLRegular
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.MainBgColor
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.common.theme.YellowLight
import com.recipes.presentation.common.utils.formatTags
import com.recipes.presentation.features.add_recipe.components.Pin
import com.recipes.presentation.features.recipes.components.AddRecipeButton
import com.recipes.presentation.features.recipes.components.FoldersMenuPopUp
import com.recipes.presentation.features.recipes.components.GapForRedPin
import com.recipes.presentation.features.recipes.components.InfoIcon
import com.recipes.presentation.features.recipes.components.LinksMenuPopUp
import com.recipes.presentation.features.recipes.components.RedPin
import com.recipes.presentation.features.recipes.components.SearchBar
import com.recipes.presentation.navigation.Screen

@Composable
fun RecipesScreenRoot(
    navController: NavController,
    viewModel: RecipesViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RecipesScreen(
        state = state,
        onAddRecipeClicked = {
            navController.navigate(Screen.AddRecipe)
        },
        onRecipeClicked = { recipeId ->
            navController.navigate(Screen.RecipeDetails(recipeId))
        },
        onAction = viewModel::onAction
    )
}

@Composable
private fun RecipesScreen(
    state: RecipesScreenState,
    onAddRecipeClicked: () -> Unit,
    onRecipeClicked: (recipeId: Int) -> Unit,
    onAction: (RecipesScreenActions) -> Unit
) {
    val d = LocalDensity.current
    val context = LocalContext.current
    var linksPopUpVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var foldersPopUpVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val hidePopUps = {
        linksPopUpVisible = false
        foldersPopUpVisible = false
    }
    var selectedFolder by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var topBarHeight by remember {
        mutableStateOf(0.dp)
    }

    val keyboardIsOpened by keyboardIsOpened()

    LaunchedEffect(keyboardIsOpened) {
        if (keyboardIsOpened) {
            hidePopUps()
        }
    }

    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MainBgColor)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    hidePopUps()
                }
                .padding(16.dp)
        ) {
            Column {

                // Title bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPlaced { topBarHeight = with(d) { it.size.height.toDp() } },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        fontFamily = FontPlayWriteCLRegular,
                        text = "Recipes",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = LightGrey
                    )
                    InfoIcon(
                        onClicked = {
                            linksPopUpVisible = !linksPopUpVisible
                            foldersPopUpVisible = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Search bar
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SearchBar(
                        onSearchTextChanged = {
                            onAction(RecipesScreenActions.OnNewSearchText(it))
                            hidePopUps()
                        },
                        onShowOnlyPinnedChanged = {
                            onAction(RecipesScreenActions.OnNewFilterPinned(it))
                            hidePopUps()
                        },
                        onFoldersIconClicked = {
                            foldersPopUpVisible = !foldersPopUpVisible
                            linksPopUpVisible = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    item {
                        // Add recipe button
                        AddRecipeButton(
                            onButtonClicked = {
                                hidePopUps()
                                onAddRecipeClicked()
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    itemsIndexed(
                        items = state.recipes,
                        key = { _, recipe -> recipe.id }
                    ) { index, recipe ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (index == 0) 0.dp else 4.dp,
                                    bottom = if (index == state.recipes.count() - 1) 0.dp else 4.dp
                                )
                                .drawNoteSheetOnBackGround(
                                    withStartAndEndGap = false
                                )
                                .padding(start = 30.dp)
                                .clickable(
                                    interactionSource = remember{ MutableInteractionSource() },
                                    indication = rememberRipple(
                                        bounded = true,
                                        color = YellowLight
                                    )
                                ) {
                                    onRecipeClicked(recipe.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(4.dp)
                            ) {
                                if (!recipe.photoUriStr.isNullOrEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(0.4f)

                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .memoryCachePolicy(CachePolicy.ENABLED)
                                                .data(Uri.parse(recipe.photoUriStr))
                                                //.data(R.drawable.dish_example)
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
                                                .height(100.dp)
                                                .rotate(-2f)
                                                .padding(2.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .padding(start = 6.dp)
                                ) {
                                    Text(
                                        text = recipe.title,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontFamily = FontEduAuvicWantHandMedium,
                                            fontSize = 18.sp,
                                            color = DarkGrey
                                        )
                                    )

                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_folder),
                                            colorFilter = ColorFilter.tint(DarkGrey),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(20.dp)
                                                .align(Alignment.CenterVertically)
                                        )
                                        Text(
                                            text = recipe.folder,
                                            modifier = Modifier,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontFamily = FontEduAuvicWantHandMedium,
                                                fontSize = 14.sp,
                                                color = DarkGrey
                                            )
                                        )
                                    }

                                    Text(
                                        fontFamily = FontEduAuvicWantHandMedium,
                                        text = recipe.tags.formatTags(),
                                        modifier = Modifier.padding(top = 4.dp),
                                        textAlign = TextAlign.Start,
                                        fontSize = 14.sp,
                                        color = CianBlueDarker
                                    )
                                }
                            }

                            androidx.compose.animation.AnimatedVisibility(
                                visible = recipe.isPinned,
                                modifier = Modifier.align(Alignment.TopEnd),
                                enter = scaleIn(),
                                exit = scaleOut()

                            ) {
                                RedPin(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(35.dp)
                                        .rotate(15f)
                                        .absoluteOffset(
                                            y = (-15).dp
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = null
                                        ) {
                                            onAction(
                                                RecipesScreenActions.OnRemoveRecipeFromPinned(
                                                    recipe.id
                                                )
                                            )
                                        }
                                )
                            }
                            androidx.compose.animation.AnimatedVisibility(
                                visible = !recipe.isPinned,
                                modifier = Modifier
                                    .align(Alignment.TopEnd),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.TopEnd)
                                        .absoluteOffset(
                                            x = 4.dp
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = null
                                        ) {
                                            onAction(RecipesScreenActions.OnPinRecipe(recipe.id))
                                        }
                                ) {
                                    GapForRedPin(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }

                            if (!recipe.photoUriStr.isNullOrEmpty()) {
                                Pin(
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(width = 10.dp, height = 30.dp)
                                        .rotate(3f),
                                    lineWidthDp = 2.dp
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = linksPopUpVisible,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 48.dp)
            ) {
                LinksMenuPopUp(
                    onLinkClicked = {
                        linksPopUpVisible = false
                    }
                )
            }

            AnimatedVisibility(
                visible = foldersPopUpVisible,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 82.dp, top = topBarHeight + 8.dp)
            ) {
                FoldersMenuPopUp(
                    folders = state.folders,
                    selectedFolder = selectedFolder,
                    onFolderClicked = { newSelectedFolder ->
                        selectedFolder = newSelectedFolder
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecipesScreenPreview() {
    val folders = mutableListOf<String>()
    for (i in 0..5) {
        folders.add("folder $i")
    }
    val recipes = mutableListOf<Recipe>()
    for (i in 0..2) {
        recipes.add(
            Recipe(
                i,
                null,
                "title $i",
                "description $i",
                "folder $i",
                "ingredients $i",
                "steps $i",
                "tags $i",
                i in 0..1
            )
        )
    }
    RecipesTheme {
        RecipesScreen(
            state = RecipesScreenState(
                folders = folders,
                recipes = recipes
            ),
            onAddRecipeClicked = {},
            onAction = {},
            onRecipeClicked = {}
        )
    }
}