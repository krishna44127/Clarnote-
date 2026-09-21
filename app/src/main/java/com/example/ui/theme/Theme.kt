package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val MidnightNavyColorScheme =
  darkColorScheme(
    primary = BrandElectricCyan,
    onPrimary = Color(0xFF0A0F1D),
    primaryContainer = Color(0xFF0F2A3F),
    onPrimaryContainer = BrandElectricCyan,
    secondary = BrandMintGreen,
    tertiary = BrandAquaBlue,
    background = Color(0xFF0A0F1D), // Deep Space Midnight Navy (#0A0F1D)
    surface = Color(0xFF131D2E), // Frosted Navy Slate (#131D2E)
    surfaceVariant = Color(0xFF1E2B45), // 1px subtle border (#1E2B45)
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF94A3B8)
  )

private val DarkGraphiteColorScheme =
  darkColorScheme(
    primary = BrandElectricCyan,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = BrandElectricCyan,
    secondary = BrandMintGreen,
    tertiary = BrandAquaBlue,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF94A3B8)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF475569),
    tertiary = AccentAmber,
    background = LightBg,
    surface = LightSurface,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569)
  )

private val AmoledColorScheme =
  darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF18181B),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF64748B),
    tertiary = AccentAmber,
    background = AMOLEDBlack,
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF1E1E1E),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFFA1A1AA)
  )

@Composable
fun MyApplicationTheme(
  appTheme: String = "Deep Space Midnight Navy",
  accentColor: String = "Electric Cyan",
  content: @Composable () -> Unit,
) {
  val chosenAccent = when (accentColor) {
    "Nordic Mint", "Neon Emerald Mint" -> NeonMint
    "Royal Indigo", "Royal Iris Violet" -> RoyalIndigo
    "Sunset Coral" -> SunsetCoral
    else -> ElectricCyan
  }

  val baseScheme = when (appTheme) {
    "True AMOLED Black" -> AmoledColorScheme
    "Clean Paper White" -> LightColorScheme
    "Dark Graphite" -> DarkGraphiteColorScheme
    else -> MidnightNavyColorScheme
  }

  val isLight = appTheme == "Clean Paper White"
  val finalScheme = baseScheme.copy(
    primary = chosenAccent,
    primaryContainer = chosenAccent.copy(alpha = 0.2f),
    onPrimary = if (isLight) Color.White else Color(0xFF0A0F1D),
    onPrimaryContainer = chosenAccent,
    tertiary = chosenAccent
  )

  MaterialTheme(colorScheme = finalScheme, typography = Typography, content = content)
}
