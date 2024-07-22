package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.presentation.common.theme.FontPlayWriteCLThin
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.GrayLighter
import com.recipes.presentation.common.theme.LightGrey
import com.recipes.presentation.common.theme.RecipesTheme

/**
 * Created by nazar at 19.07.2024
 * This component is used for...
 */
@Composable
fun FoldersMenuPopUp(
    folders: List<String>,
    modifier: Modifier = Modifier,
    selectedFolder: String? = null,
    onFolderClicked: (folder: String?) -> Unit
) {
    val d = LocalDensity.current

    var lineWidth by remember {
        mutableStateOf(0.dp)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Gray)
            .onPlaced { lineWidth = with(d) { it.size.width.toDp() } }
    ) {
        folders.forEachIndexed { index, folder ->
            Row(
                modifier = Modifier
                    .widthIn(min = lineWidth)
                    .clickable {
                        if (selectedFolder == folder) {
                            onFolderClicked(null)
                        } else {
                            onFolderClicked(folder)
                        }
                    }
                    .padding(
                        bottom = if (index == folders.count() - 1) 8.dp else 4.dp,
                        top = if (index == 0) 8.dp else 4.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = folder == selectedFolder,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .background(
                                    color = LightGrey,
                                    shape = CircleShape
                                )
                                .size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = folder != selectedFolder && !selectedFolder.isNullOrEmpty(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Row {
                        Spacer(
                            modifier = Modifier
                                .size(12.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                Text(
                    fontFamily = FontPlayWriteCLThin,
                    text = folder,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = LightGrey
                )
            }
            if (index != folders.count() - 1) {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .background(GrayLighter)
                        .width(lineWidth)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FoldersMenuPopUpPreview() {
    val folders = mutableListOf<String>()
    for (i in 0..5) {
        folders.add("folder $i")
    }
    RecipesTheme {
        FoldersMenuPopUp(
            folders = folders,
            selectedFolder = "folder 2",
            onFolderClicked = {}
        )
    }
}