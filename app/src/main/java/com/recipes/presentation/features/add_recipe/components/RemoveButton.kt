package com.recipes.presentation.features.add_recipe.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.GrayLighterAlpha100
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun RemoveButton(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(30.dp)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(
                    bounded = false,
                    radius = 45.dp,
                    color = Gray
                )
            ) {
                onClicked()
            }
    ) {
        drawCircle(
            color = GrayLighterAlpha100
        )
        drawCircle(
            color = Gray,
            style = Stroke(
                width = 1.dp.toPx()
            )
        )
        drawLine(
            color = Gray,
            start = size.center - Offset(
                size.height / 4,
                size.width / 4
            ),
            end = size.center + Offset(size.height / 4, size.width / 4),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Gray,
            start = Offset(
                size.center.x - size.height / 4,
                size.center.y + size.width / 4
            ),
            end = Offset(
                size.center.x + size.height / 4,
                size.center.y - size.width / 4
            ),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Preview
@Composable
private fun RemoveButtonPreview() {
    RecipesTheme {
        RemoveButton({})
    }
}