package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun GapForRedPin(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .size(15.dp)
            .drawBehind {
                val radius = size.width / 2
                drawCircle(
                    color = DarkGrey,
                    radius = radius,
                    center = size.center,
                    style = Stroke(
                        width = size.width / 7
                    )
                )
            }
    )
}

@Preview
@Composable
private fun GapForRedPinPreview() {
    RecipesTheme {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
            GapForRedPin()
        }
    }
}