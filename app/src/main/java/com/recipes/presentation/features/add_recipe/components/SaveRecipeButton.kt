package com.recipes.presentation.features.add_recipe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.presentation.common.theme.FontEduAuvicWantHandBold
import com.recipes.presentation.common.theme.Green

@Composable
fun SaveRecipeButton(
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var height by remember {
        mutableStateOf(0.dp)
    }
    val d = LocalDensity.current
    Column {
        Box(
            modifier = modifier
                .background(Color.Transparent)
                .clip(RoundedCornerShape(height))
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = rememberRipple(
                        color = Green
                    )
                ) {
                    onButtonClicked()
                }
                .drawBehind {
                    inset(1.dp.toPx()) {
                        drawRoundRect(
                            color = Green,
                            topLeft = Offset.Zero,
                            size = size,
                            cornerRadius = CornerRadius(100f, 100f),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
                .padding(vertical = 12.dp, horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .onPlaced {
                        height = with(d) { it.size.height.toDp() }
                    }
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                        .drawBehind {
                            val lineWidth = 2.dp.toPx()

                            val p = Path()
                            p.moveTo(
                                x = center.x - size.width / 2,
                                y = center.y
                            )
                            val line1Point1X = center.x - size.width / 2
                            val line1Point1Y = center.y
                            p.cubicTo(
                                x1 = line1Point1X,
                                y1 = line1Point1Y,
                                x2 = center.x - (center.x - line1Point1X) / 3,
                                y2 = center.y,
                                x3 = center.x,
                                y3 = center.y + size.height / 2 - 1.dp.toPx(),
                            )

                            val line2Point1X = center.x
                            val line2Point1Y = center.y + size.height / 2 - 1.dp.toPx()
                            p.cubicTo(
                                x1 = line2Point1X,
                                y1 = line2Point1Y,
                                x2 = center.x,
                                y2 = center.y,
                                x3 = center.x + size.width / 2,
                                y3 = center.y - size.height / 2,
                            )

                            // draw path
                            drawPath(
                                path = p,
                                color = Green,
                                style = Stroke(width = lineWidth, cap = StrokeCap.Round)
                            )
                        }
                )
                Text(
                    fontFamily = FontEduAuvicWantHandBold,
                    text = "Save recipe",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Green
                )
            }
        }
    }
}

@Preview
@Composable
private fun SaveRecipeButtonPreview() {
    SaveRecipeButton({})
}