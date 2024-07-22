package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun LiteGrayPin(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .width(50.dp)
            .height(100.dp)
            .drawBehind {
                val pinRadius = (size.width * 0.7f) / 2

                val p = Path()

                p.moveTo(
                    x = size.center.x - 3.dp.toPx() / 2,
                    y = 0f + pinRadius * 2
                )
                p.lineTo(
                    x = size.center.x + 3.dp.toPx() / 2,
                    y = 0f + pinRadius * 2
                )
                p.lineTo(
                    x = size.center.x + 3.dp.toPx() / 2,
                    y = size.height * 0.9f - 12.dp.toPx() / 2
                )
                p.lineTo(
                    x = size.center.x,
                    y = size.height * 0.9f
                )
                p.lineTo(
                    x = size.center.x - 3.dp.toPx() / 2,
                    y = size.height * 0.9f - 12.dp.toPx() / 2
                )
                p.lineTo(
                    x = size.center.x - 3.dp.toPx() / 2,
                    y = 0f + pinRadius * 2
                )

                drawPath(
                    p,
                    color = LightGrey,
                    style = Fill
                )

                drawCircle(
                    color = LightGrey,
                    radius = pinRadius,
                    center = Offset(size.center.x, 0f + pinRadius),
                    style = Stroke(
                        width = 3.dp.toPx()
                    )
                )
            }
    )
}

@Preview
@Composable
private fun LiteGrayPinPreview() {
    RecipesTheme {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
            LiteGrayPin()
        }
    }
}