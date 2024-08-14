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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.common.theme.RedPinColor

@Composable
fun RedPin(
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
                    color = DarkGrey,
                    style = Fill
                )

                drawCircle(
                    color = RedPinColor,
                    radius = pinRadius,
                    center = Offset(size.center.x, 0f + pinRadius)
                )

                val whiteCircleRadius = pinRadius / 5
                drawCircle(
                    color = Color.White,
                    radius = whiteCircleRadius,
                    center = Offset(
                        size.center.x - whiteCircleRadius * 1.6f, 0f + pinRadius / 2
                    )
                )

                val whiteCircleSmallerRadius = whiteCircleRadius / 2
                drawCircle(
                    color = Color.White,
                    radius = whiteCircleSmallerRadius,
                    center = Offset(
                        size.center.x - whiteCircleRadius * 1.6f - whiteCircleRadius * 1.1f,
                        0f + pinRadius / 2 + whiteCircleRadius * 2f
                    )
                )
            }
    )
}

@Preview
@Composable
private fun RedPinPreview() {
    RecipesTheme {
        Box(
            modifier = Modifier.padding(0.dp)
        ) {
            RedPin()
        }
    }
}