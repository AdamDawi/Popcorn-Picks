package com.adamdawi.popcornpicks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.R

val fontFamily = FontFamily(
    Font(R.font.josefinsans_regular, FontWeight.Normal),
    Font(R.font.josefinsans_bold, FontWeight.Bold),
    Font(R.font.josefinsans_semibold, FontWeight.SemiBold),
    Font(R.font.josefinsans_medium, FontWeight.Medium),
    Font(R.font.josefinsans_light, FontWeight.Light),
    Font(R.font.josefinsans_extralight, FontWeight.ExtraLight),
    Font(R.font.josefinsans_thin, FontWeight.Thin),
    Font(R.font.josefinsans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.josefinsans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.josefinsans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.josefinsans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.josefinsans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.josefinsans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.josefinsans_thinitalic, FontWeight.Thin, FontStyle.Italic)
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)