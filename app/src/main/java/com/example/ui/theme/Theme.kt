package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val StorybookColorScheme = lightColorScheme(
    primary = HoneyGold,
    onPrimary = WarmInk,
    primaryContainer = SunbeamYellow,
    onPrimaryContainer = WarmInk,
    secondary = MeadowGreen,
    onSecondary = Color.White,
    secondaryContainer = MintWash,
    onSecondaryContainer = MeadowGreenDark,
    tertiary = SkyBlue,
    onTertiary = Color.White,
    tertiaryContainer = SkyWash,
    onTertiaryContainer = SkyBlueDark,
    background = ParchmentCream,
    onBackground = WarmInk,
    surface = WarmPaper,
    onSurface = WarmInk,
    surfaceVariant = SoftLinen,
    onSurfaceVariant = CocoaMuted,
    outline = OutlineSand,
    error = CoralDark,
    onError = Color.White,
    errorContainer = BlushWash,
    onErrorContainer = CoralDark
)

val StorybookShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun BibleKidsTheme(
    dyslexiaFriendly: Boolean = false,
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StorybookColorScheme,
        typography = buildAppTypography(
            dyslexiaFriendly = dyslexiaFriendly,
            fontScale = fontScale
        ),
        shapes = StorybookShapes,
        content = content
    )
}
