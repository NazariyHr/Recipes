package com.recipes.presentation.features.add_recipe.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.CianBlue
import com.recipes.presentation.common.theme.LightGreen
import com.recipes.presentation.common.theme.RecipesTheme
import com.recipes.presentation.common.theme.YellowLight
import kotlin.math.roundToInt

fun Modifier.drawNoteSheetOnBackGround(): Modifier =
    this
        .background(
            YellowLight,
            RoundedCornerShape(8.dp)
        )
        .drawBehind {
            val gap = 30.dp.toPx()
            val height = size.height
            val width = size.width
            val lineWidth = 1.dp.toPx() / 2
            val verticalLinesCount = (height / gap).toInt()
            val horizontalLinesCount = (width / gap).toInt()

            for (i in 1..verticalLinesCount) {
                drawLine(
                    color = CianBlue,
                    start = Offset(0f, gap * i),
                    end = Offset(width, gap * i),
                    strokeWidth = lineWidth
                )
            }
            for (i in 1..horizontalLinesCount) {
                drawLine(
                    color = CianBlue,
                    start = Offset(gap * i, 0f),
                    end = Offset(gap * i, height),
                    strokeWidth = lineWidth
                )
            }

            val paddingForDots = 12.dp.toPx()
            val startPadding = 20.dp.toPx()
            val endPaddingValues = 20.dp.toPx()
            val dotWidth = 20.dp.toPx()
            val gapBetweenDots = 20.dp.toPx()
            val dotsCount = (height / (dotWidth + gapBetweenDots / 2)).toInt() -
                    ((startPadding + endPaddingValues) / (dotWidth + gapBetweenDots / 2)).roundToInt()

            for (i in 1..dotsCount) {
                drawCircle(
                    color = LightGreen,
                    radius = dotWidth / 2,
                    center = Offset(
                        paddingForDots,
                        (gapBetweenDots + dotWidth / 2) * i + startPadding
                    )
                )

                drawLine(
                    color = LightGreen,
                    start = Offset(
                        paddingForDots,
                        (gapBetweenDots + dotWidth / 2) * i + startPadding
                    ),
                    end = Offset(
                        0f,
                        (gapBetweenDots + dotWidth / 2) * i + startPadding
                    ),
                    strokeWidth = dotWidth / 1.4f
                )
            }
        }

@Preview
@Composable
private fun NoteSheetOnBackgroundPreview() {
    RecipesTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawNoteSheetOnBackGround()
        ) { }
    }
}