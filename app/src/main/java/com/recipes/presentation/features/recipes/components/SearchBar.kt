package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke.Companion.DefaultCap
import androidx.compose.ui.graphics.drawscope.Stroke.Companion.DefaultJoin
import androidx.compose.ui.graphics.drawscope.Stroke.Companion.DefaultMiter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.R
import com.recipes.presentation.common.components.keyboardIsOpened
import com.recipes.presentation.common.theme.FontEduAuvicWantHandRegular
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.RecipesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by nazar at 19.07.2024
 * This component is used for...
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    maxTextLength: Int = 50,
    onSearchTextChanged: (searchText: String) -> Unit,
    onShowOnlyPinnedChanged: (showOnlyFromBookmarks: Boolean) -> Unit,
    onFoldersIconClicked: () -> Unit,
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    var fieldIsFocused by rememberSaveable {
        mutableStateOf(false)
    }
    var showOnlyPinned by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val requester = FocusRequester()
    val keyboardIsOpened by keyboardIsOpened()

    LaunchedEffect(keyboardIsOpened) {
        if (!keyboardIsOpened) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(100L)
                withContext(Dispatchers.Main) {
                    if (fieldIsFocused) {
                        focusManager.clearFocus()
                    }
                }
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        BasicTextField(
            modifier = Modifier
                .focusRequester(requester)
                .onFocusChanged { focusState ->
                    fieldIsFocused = focusState.isFocused
                }
                .drawBehind {
                    drawOutline(
                        outline = Outline.Rounded(
                            RoundRect(
                                left = 0f,
                                top = 0f,
                                right = size.width,
                                bottom = size.height,
                                radiusX = 4.dp.toPx(),
                                radiusY = 4.dp.toPx()
                            )
                        ),
                        color = LightGrey,
                        style = Stroke(
                            1.dp.toPx(),
                            DefaultMiter,
                            DefaultCap,
                            DefaultJoin
                        )
                    )
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .weight(1f),
            value = searchText,
            onValueChange = { txt ->
                if (txt.length <= maxTextLength) {
                    searchText = txt
                    onSearchTextChanged(searchText)
                }
            },
            textStyle = TextStyle(
                fontFamily = FontEduAuvicWantHandRegular,
                fontSize = 16.sp,
                color = LightGrey
            ),
            cursorBrush = SolidColor(LightGrey),
            singleLine = true
        ) { innerTextField ->
            if (fieldIsFocused) {
                innerTextField()
            }
            val placeHolder = "Search"
            if (searchText.isEmpty()) {
                Text(
                    fontFamily = FontEduAuvicWantHandRegular,
                    text = placeHolder,
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = LightGrey
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_folder),
            colorFilter = ColorFilter.tint(LightGrey),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 32.dp,
                        color = Gray
                    )
                ) {
                    onFoldersIconClicked()
                }
                .size(32.dp)
                .align(Alignment.CenterVertically)
        )
        if (showOnlyPinned) {
            RedPin(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
                    .padding(start = 8.dp, end = 4.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 32.dp,
                            color = Gray
                        )
                    ) {
                        showOnlyPinned = !showOnlyPinned
                        onShowOnlyPinnedChanged(showOnlyPinned)
                    }
            )
        } else {
            LiteGrayPin(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
                    .padding(start = 8.dp, end = 4.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 32.dp,
                            color = Gray
                        )
                    ) {
                        showOnlyPinned = !showOnlyPinned
                        onShowOnlyPinnedChanged(showOnlyPinned)
                    }
            )
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    RecipesTheme {
        SearchBar(
            onSearchTextChanged = {},
            onShowOnlyPinnedChanged = {},
            onFoldersIconClicked = {}
        )
    }
}