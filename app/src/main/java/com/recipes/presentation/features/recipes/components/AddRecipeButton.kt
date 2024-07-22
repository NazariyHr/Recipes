package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.LightGrey

/**
 * Created by nazar at 18.07.2024
 * It's button to add new recipes
 */
@Composable
fun AddRecipeButton(
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .clip(RoundedCornerShape(22.dp))
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = rememberRipple(
                        color = Gray
                    )
                ) {
                    onButtonClicked()
                }
                .drawBehind {
                    val linesLength = size.height / 13f * 8f
                    inset(1.dp.toPx()) {
                        drawRoundRect(
                            color = LightGrey,
                            topLeft = Offset.Zero,
                            size = size,
                            cornerRadius = CornerRadius(100f, 100f),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )

                        drawLine(
                            color = LightGrey,
                            start = Offset(
                                x = center.x - linesLength / 2,
                                y = center.y,
                            ),
                            end = Offset(
                                x = center.x + linesLength / 2,
                                y = center.y,
                            ),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        drawLine(
                            color = LightGrey,
                            start = Offset(
                                x = center.x,
                                y = center.y - linesLength / 2,
                            ),
                            end = Offset(
                                x = center.x,
                                y = center.y + linesLength / 2,
                            ),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
                .padding(vertical = 12.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AddRecipeButtonPreview() {
    AddRecipeButton({})
}