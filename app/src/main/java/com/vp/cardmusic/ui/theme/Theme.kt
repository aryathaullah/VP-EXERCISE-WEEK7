package com.vp.cardmusic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AssignmentColors = darkColorScheme(
    primary = AppColors.MusicGreen,
    secondary = AppColors.PokemonRed,
    background = AppColors.Background,
    surface = AppColors.Surface,
    onPrimary = AppColors.TextPrimary,
    onSecondary = AppColors.TextPrimary,
    onBackground = AppColors.TextPrimary,
    onSurface = AppColors.TextPrimary,
    error = AppColors.ErrorRed,
)

private val AssignmentTypography = Typography(
    displayLarge = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    headlineMedium = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 24.sp),
    titleLarge = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
    bodyLarge = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodyMedium = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelMedium = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 11.sp),
)

@Composable
fun AssignmentTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AssignmentColors,
        typography = AssignmentTypography,
        content = content,
    )
}
