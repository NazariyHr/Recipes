package com.recipes.presentation.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.recipes.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val FontPlayWriteCLRegular = FontFamily(Font(R.font.playwritecl_regular))
val FontPlayWriteCLThin = FontFamily(Font(R.font.playwritecl_thin))
val FontPlayWriteCLExtraThin = FontFamily(Font(R.font.playwritecl_extra_thin))
val FontPlayWriteCLLight = FontFamily(Font(R.font.playwritecl_light))

val FontEduAuvicWantHandRegular = FontFamily(Font(R.font.edu_auvic_want_hand_regular))
val FontEduAuvicWantHandMedium = FontFamily(Font(R.font.edu_auvic_want_hand_medium))
val FontEduAuvicWantHandBold = FontFamily(Font(R.font.edu_auvic_want_hand_bold))
val FontEduAuvicWantHandSemiBold = FontFamily(Font(R.font.edu_auvic_want_hand_semi_bold))

val DefaultFont = FontPlayWriteCLRegular