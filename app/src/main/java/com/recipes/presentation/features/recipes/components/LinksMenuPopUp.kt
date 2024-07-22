package com.recipes.presentation.features.recipes.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recipes.R
import com.recipes.presentation.common.theme.FontPlayWriteCLThin
import com.recipes.presentation.common.theme.Gray
import com.recipes.presentation.common.theme.GrayLighter
import com.recipes.presentation.common.theme.LightGrey

/**
 * Created by nazar at 19.07.2024
 * This component is used for...
 */
@Composable
fun LinksMenuPopUp(
    onLinkClicked: (link: String) -> Unit,
    modifier: Modifier = Modifier
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
        Row(
            modifier = Modifier
                .widthIn(min = lineWidth)
                .clickable {
                    onLinkClicked("")
                }
                .padding(bottom = 4.dp, top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_privacy_policy),
                colorFilter = ColorFilter.tint(LightGrey),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                fontFamily = FontPlayWriteCLThin,
                text = "Privacy policy",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = LightGrey
            )
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(GrayLighter)
                .width(lineWidth)
        )
        Row(
            modifier = Modifier
                .widthIn(min = lineWidth)
                .clickable {
                    onLinkClicked("")
                }
                .padding(bottom = 8.dp, top = 4.dp, start = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_contact_us),
                colorFilter = ColorFilter.tint(LightGrey),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                fontFamily = FontPlayWriteCLThin,
                text = "Contact Us",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = LightGrey
            )
        }
    }
}

@Preview
@Composable
private fun LinksMenuPopUpPreview() {
    LinksMenuPopUp({})
}