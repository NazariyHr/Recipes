package com.recipes.presentation.features.add_recipe.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.RecipesTheme

@Composable
fun Pin(
    modifier: Modifier = Modifier,
    lineWidthDp: Dp = 4.dp
) {
    Canvas(
        modifier = modifier
            .size(width = 30.dp, height = 70.dp)
    ) {
        val lineWidth = lineWidthDp.toPx()
        val radius = size.width / 30 * 22
        val halfRadius = radius / 2

        val p = Path()

        //top left corner
        p.moveTo(0f, 0f)
        p.relativeMoveTo(halfRadius, -halfRadius)
        p.cubicTo(halfRadius, -halfRadius, 0f, -halfRadius, 0f, 0f)

        //top side
        p.moveTo(
            halfRadius,
            -halfRadius
        )
        p.lineTo(
            size.width - halfRadius,
            -halfRadius
        )

        //top right corner
        p.moveTo(size.width, 0f)
        p.relativeMoveTo(-halfRadius, -halfRadius)
        p.cubicTo(
            size.width - halfRadius,
            -halfRadius,
            size.width,
            -halfRadius,
            size.width,
            halfRadius
        )

        //right side
        p.moveTo(
            size.width,
            halfRadius
        )
        p.lineTo(
            size.width,
            size.height - halfRadius
        )

        // bottom right corner
        p.moveTo(
            size.width,
            size.height - halfRadius
        )
        p.cubicTo(
            size.width,
            size.height - halfRadius,
            size.width,
            size.height,
            size.width - halfRadius,
            size.height
        )

        //bottom side
        p.moveTo(
            size.width - halfRadius,
            size.height
        )
        p.lineTo(
            halfRadius + lineWidth,
            size.height
        )

        // bottom left corner
        p.moveTo(
            halfRadius + lineWidth,
            size.height
        )
        p.cubicTo(
            halfRadius + lineWidth,
            size.height,
            0f + lineWidth,
            size.height,
            0f + lineWidth,
            size.height - halfRadius
        )

        //left side
        p.moveTo(
            0f + lineWidth,
            size.height - halfRadius
        )
        p.lineTo(
            0f + lineWidth,
            size.height - halfRadius - size.height / 1.7f
        )

        // inner top left corner
        p.moveTo(
            0f + lineWidth,
            size.height - halfRadius - size.height / 1.7f
        )
        p.cubicTo(
            0f + lineWidth,
            size.height - halfRadius - size.height / 1.7f,
            0f + lineWidth,
            size.height - halfRadius - size.height / 1.7f - halfRadius,
            0f + lineWidth + halfRadius,
            size.height - halfRadius - size.height / 1.7f - halfRadius
        )

        // inner top right corner
        p.moveTo(
            0f + lineWidth + halfRadius,
            size.height - halfRadius - size.height / 1.7f - halfRadius
        )
        p.cubicTo(
            0f + lineWidth + halfRadius,
            size.height - halfRadius - size.height / 1.7f - halfRadius,
            0f + lineWidth + halfRadius + halfRadius - lineWidth / 2,
            size.height - halfRadius - size.height / 1.7f - halfRadius,
            0f + lineWidth + halfRadius + halfRadius - lineWidth / 2,
            size.height - halfRadius - size.height / 1.7f,
        )

        //inner right side
        p.moveTo(
            0f + lineWidth + halfRadius + halfRadius - lineWidth / 2,
            size.height - halfRadius - size.height / 1.7f
        )
        p.lineTo(
            0f + lineWidth + halfRadius + halfRadius - lineWidth / 2,
            size.height - halfRadius
        )

        // draw
        drawPath(
            path = p,
            color = Gray,
            style = Stroke(width = lineWidth)
        )
    }
}

@Preview
@Composable
private fun PinPreview() {
    RecipesTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            Pin()
        }
    }
}