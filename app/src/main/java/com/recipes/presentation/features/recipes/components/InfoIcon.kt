package com.recipes.presentation.features.recipes.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.R
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.LightGrey

/**
 * Created by nazar at 19.07.2024
 * This component is used for...
 */
@Composable
fun InfoIcon(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        var iconClickAnimTrigger by remember { mutableStateOf(true) }
        AnimatedContent(
            modifier = Modifier
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 32.dp,
                        color = Gray
                    ),
                    onClick = {
                        iconClickAnimTrigger = !iconClickAnimTrigger
                        onClicked()
                    }
                ),
            targetState = iconClickAnimTrigger,
            contentAlignment = Alignment.Center,
            transitionSpec = {
                scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) togetherWith
                        scaleOut(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
            }, label = ""
        ) { state ->
            state
            Image(
                painter = painterResource(id = R.drawable.ic_info),
                colorFilter = ColorFilter.tint(LightGrey),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun InfoIconPreview() {
    InfoIcon({})
}