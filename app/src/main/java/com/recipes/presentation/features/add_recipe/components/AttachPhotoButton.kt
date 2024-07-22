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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.FontEduAuvicWantHandBold

@Composable
fun AttachPhotoButton(
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
                        color = DarkGrey
                    )
                ) {
                    onButtonClicked()
                }
                .drawBehind {
                    inset(1.dp.toPx()) {
                        drawRoundRect(
                            color = DarkGrey,
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
                        .size(16.dp)
                        .padding(end = 12.dp)
                        .align(Alignment.CenterVertically)
                        .drawBehind {
                            val linesLength = size.height

                            drawLine(
                                color = DarkGrey,
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
                                color = DarkGrey,
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
                )
                Text(
                    fontFamily = FontEduAuvicWantHandBold,
                    text = "Attach a photo",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = DarkGrey
                )
            }
        }
    }
}

@Preview
@Composable
private fun AttachPhotoButtonPreview() {
    AttachPhotoButton({})
}