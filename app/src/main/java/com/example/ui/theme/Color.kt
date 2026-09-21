package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ClarNote Exact Brand Palette
val MidnightNavyBg = Color(0xFF0A0F1D) // Deep Space Midnight Navy (#0A0F1D)
val MidnightNavyCanvasStart = Color(0xFF0A0F1D)
val MidnightNavyCanvasEnd = Color(0xFF131D2E)
val FrostedSlateSurface = Color(0xFF131D2E) // Frosted Navy Slate
val FrostedSlateBorder = Color(0xFF1E2B45)

val BrandElectricCyan = Color(0xFF00D2FF)
val BrandAquaBlue = Color(0xFF00E5D4)
val BrandMintGreen = Color(0xFF10B981)
val BrandCornerFoldDark = Color(0xFF059669)
val BrandSparkleGlow = Color(0x99E0F7FA)

// Quick Action Badges matching prompt exact specs
val BadgeCanvasIcon = Color(0xFF818CF8)
val BadgeCanvasBg = Color(0xFF1E1B4B)

val BadgeDocsIcon = Color(0xFFF87171)
val BadgeDocsBg = Color(0xFF450A0A)

val BadgeCameraIcon = Color(0xFF38BDF8)
val BadgeCameraBg = Color(0xFF0C4A6E)

val BadgeScannerIcon = Color(0xFF06B6D4)
val BadgeScannerBg = Color(0xFF164E63)

val BadgeGalleryIcon = Color(0xFF34D399)
val BadgeGalleryBg = Color(0xFF064E3B)

val BadgeFolderIcon = Color(0xFFFBBF24)
val BadgeFolderBg = Color(0xFF451A03)

// Controlled Vibrant Subject Palette (Consistent & Cool)
val SubjectPhysics = Color(0xFF00D2FF)      // Electric Cyan Glow (#00D2FF)
val SubjectChemistry = Color(0xFF10B981)    // Neon Emerald Mint (#10B981)
val SubjectBiology = Color(0xFFFB7185)      // Sunset Coral Rose (#FB7185)
val SubjectMathematics = Color(0xFF818CF8)  // Royal Iris Violet (#818CF8)
val SubjectMath = SubjectMathematics
val SubjectCS = Color(0xFF8B5CF6)
val SubjectMistakeDiary = Color(0xFFF43F5E)
val SubjectVault = Color(0xFF6366F1)

// Signature Brand Gradient: Electric Cyan -> Aqua Blue -> Emerald Mint
val ClarNoteBrandGradient = Brush.linearGradient(
    colors = listOf(BrandElectricCyan, BrandAquaBlue, BrandMintGreen)
)

val ClarNoteCanvasGradient = Brush.verticalGradient(
    colors = listOf(MidnightNavyCanvasStart, MidnightNavyBg, MidnightNavyCanvasEnd)
)

val PrimaryBlue = BrandElectricCyan
val PrimaryBlueLight = Color(0xFF38BDF8)
val DarkNavyBg = MidnightNavyBg
val DarkNavySurface = FrostedSlateSurface
val LightBg = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)

val AccentAmber = Color(0xFFFBBF24)
val SuccessEmerald = Color(0xFF10B981)
val DangerRose = Color(0xFFEF4444)
val ElectricCyan = BrandElectricCyan
val NeonMint = BrandMintGreen
val RoyalIndigo = Color(0xFF818CF8)
val SunsetCoral = Color(0xFFFB7185)
val AMOLEDBlack = Color(0xFF000000)


