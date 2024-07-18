package com.recipes.presentation.features.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.recipes.R
import com.recipes.presentation.common.theme.MainBgColor
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun RecipesScreenRoot(
    navController: NavController,
    viewModel: RecipesViewModel =
        hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RecipesScreen(
        state = state
    )
}

@Composable
private fun RecipesScreen(
    state: RecipesScreenState
) {
    Scaffold(
        modifier = Modifier
            .background(color = MainBgColor)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.main_bg_2),
                    contentScale = ContentScale.Crop
                )
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier.padding(
                    16.dp
                )
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun RecipesScreenPreview() {
    RecipesTheme {
        RecipesScreen(
            state = RecipesScreenState(

            )
        )
    }
}