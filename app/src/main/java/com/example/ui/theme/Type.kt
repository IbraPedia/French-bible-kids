package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val FredokaFontFamily = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal),
    Font(R.font.fredoka, FontWeight.SemiBold),
    Font(R.font.fredoka, FontWeight.Bold)
)

val NunitoFontFamily = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito, FontWeight.SemiBold),
    Font(R.font.nunito, FontWeight.Bold)
)

val LexendFontFamily = FontFamily(
    Font(R.font.lexend, FontWeight.Normal),
    Font(R.font.lexend, FontWeight.SemiBold),
    Font(R.font.lexend, FontWeight.Bold)
)

fun buildAppTypography(
    dyslexiaFriendly: Boolean = false,
    fontScale: Float = 1.0f
): Typography {
    val displayFamily = if (dyslexiaFriendly) LexendFontFamily else FredokaFontFamily
    val bodyFamily = if (dyslexiaFriendly) LexendFontFamily else NunitoFontFamily
    val extraSpacing = if (dyslexiaFriendly) 0.9.sp else 0.15.sp
    val lineBoost = if (dyslexiaFriendly) 1.35f else 1.25f

    return Typography(
        displayLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (32f * fontScale).sp,
            lineHeight = (38f * fontScale).sp,
            letterSpacing = extraSpacing
        ),
        headlineMedium = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (24f * fontScale).sp,
            lineHeight = (30f * fontScale).sp,
            letterSpacing = extraSpacing
        ),
        titleLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (20f * fontScale).sp,
            lineHeight = (26f * fontScale).sp,
            letterSpacing = extraSpacing
        ),
        titleMedium = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (17f * fontScale).sp,
            lineHeight = (22f * fontScale).sp,
            letterSpacing = extraSpacing
        ),
        bodyLarge = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (17f * fontScale).sp,
            lineHeight = (17f * fontScale * lineBoost * 1.15f).sp,
            letterSpacing = extraSpacing
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (15f * fontScale).sp,
            lineHeight = (15f * fontScale * lineBoost).sp,
            letterSpacing = extraSpacing
        ),
        labelLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (15f * fontScale).sp,
            lineHeight = (20f * fontScale).sp,
            letterSpacing = extraSpacing
        ),
        labelSmall = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (12f * fontScale).sp,
            lineHeight = (16f * fontScale).sp,
            letterSpacing = extraSpacing
        )
    )
}

val Typography = buildAppTypography()
