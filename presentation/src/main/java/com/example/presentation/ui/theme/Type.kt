package com.example.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presentation.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
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

val robots = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.roboto_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.roboto_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.roboto_thin, FontWeight.Thin, FontStyle.Normal)
)

val KCSTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = robots,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = robots,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = robots,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp,
    )
)