package com.recipes.presentation.features.recipes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.recipes.presentation.common.components.keyboardIsOpened
import com.recipes.presentation.common.theme.FontPlayWriteCLRegular
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.MainBgColor
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.features.recipes.components.AddRecipeButton
import com.recipes.presentation.features.recipes.components.FoldersMenuPopUp
import com.recipes.presentation.features.recipes.components.InfoIcon
import com.recipes.presentation.features.recipes.components.LinksMenuPopUp
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
        }
    )
}

@Composable
private fun RecipesScreen(
    state: RecipesScreenState,
    onAddRecipeClicked: () -> Unit
) {
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
    var searchText by rememberSaveable {
        mutableStateOf("")
    }
    var showOnlyFromBookmarks by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedFolder by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    var topBarHeight by remember {
        mutableStateOf(0.dp)
    }
    val d = LocalDensity.current

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
                .padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    hidePopUps()
                }
                .padding(16.dp)
        ) {
            Column {
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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SearchBar(
                        onSearchTextChanged = {
                            searchText = it
                            hidePopUps()
                        },
                        onShowOnlyFromBookmarksChanged = {
                            showOnlyFromBookmarks = it
                            hidePopUps()
                        },
                        onFoldersIconClicked = {
                            foldersPopUpVisible = !foldersPopUpVisible
                            linksPopUpVisible = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                AddRecipeButton(
                    onButtonClicked = {
                        hidePopUps()
                        onAddRecipeClicked()
                    }
                )

                //todo : list with receipts
                //todo : recipe details screen
                //todo : remove recipe
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
    RecipesTheme {
        RecipesScreen(
            state = RecipesScreenState(
                folders = folders
            ),
            onAddRecipeClicked = {}
        )
    }
}