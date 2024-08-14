package com.recipes.presentation.features.add_recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.presentation.common.components.keyboardIsOpened
import com.recipes.presentation.common.theme.DarkGrey
import com.recipes.presentation.common.theme.FontEduAuvicWantHandMedium
import com.recipes.presentation.common.theme.RecipesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NotesTextField(
    onTextChanged: (String) -> Unit,
    placeHolder: String,
    maxLines: Int,
    maxLength: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    fontFamily: FontFamily = FontEduAuvicWantHandMedium,
    onFocusChanged: (focused: Boolean) -> Unit = {},
    textFromRemoteHandler: ((textFromRemote: String) -> Unit) -> Unit = {},
    customTextDrawComposable: (@Composable (text: String) -> Unit)? = null,
    maxHeightWhenFocused: Dp = Dp.Unspecified,
    allowNewLines: Boolean = true,
    imeAction: ImeAction? = null,
    keyboardActions: KeyboardActions? = null
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val focusManager = LocalFocusManager.current
    val requester = FocusRequester()
    val keyboardIsOpened by keyboardIsOpened()
    var fieldIsFocused by rememberSaveable {
        mutableStateOf(false)
    }

    textFromRemoteHandler { textFromRemote ->
        textFieldValue = textFieldValue.copy(
            text = textFromRemote
        )
        onTextChanged(textFromRemote)
    }

    val maxHeight by remember(maxHeightWhenFocused, fieldIsFocused) {
        derivedStateOf {
            if (fieldIsFocused) maxHeightWhenFocused
            else Dp.Unspecified
        }
    }

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
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        BasicTextField(
            modifier = modifier
                .focusRequester(requester)
                .onFocusChanged { focusState ->
                    fieldIsFocused = focusState.isFocused
                    onFocusChanged(fieldIsFocused)
                }
                .drawBehind {
                    if (
                        (keyboardIsOpened && fieldIsFocused) ||
                        textFieldValue.text.isEmpty()
                    ) {
                        drawLine(
                            color = DarkGrey,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
                .heightIn(max = maxHeight),
            value = textFieldValue,
            onValueChange = { txt ->
                if (txt.text.length <= maxLength) {
                    val newText = if (!allowNewLines) {
                        txt.text.replace("\n", "")
                    } else {
                        txt.text
                    }
                    textFieldValue = txt.copy(
                        text = newText
                    )
                    onTextChanged(newText)
                }
            },
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontSize = fontSize,
                color = DarkGrey
            ),
            cursorBrush = SolidColor(DarkGrey),
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction ?: ImeAction.Unspecified
            ),
            keyboardActions = keyboardActions ?: KeyboardActions.Default
        ) { innerTextField ->
            if (fieldIsFocused) {
                innerTextField()
            }
            if (textFieldValue.text.isEmpty()) {
                Text(
                    fontFamily = fontFamily,
                    text = placeHolder,
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    fontSize = fontSize,
                    color = DarkGrey
                )
            }
        }
        if (!fieldIsFocused && textFieldValue.text.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        requester.requestFocus()
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(textFieldValue.text.length, textFieldValue.text.length)
                        )
                    }
            ) {
                if (customTextDrawComposable != null) {
                    customTextDrawComposable(textFieldValue.text)
                } else {
                    Text(
                        text = textFieldValue.text,
                        modifier = Modifier,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = fontSize,
                            color = DarkGrey
                        )
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun NotesTextFieldPreview() {
    RecipesTheme {
        NotesTextField(
            onTextChanged = {},
            placeHolder = "Description",
            maxLines = 5,
            maxLength = 50
        )
    }
}