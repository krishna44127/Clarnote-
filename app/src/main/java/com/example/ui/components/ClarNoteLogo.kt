package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandAquaBlue
import com.example.ui.theme.BrandCornerFoldDark
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.BrandMintGreen
import com.example.ui.theme.BrandSparkleGlow
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.MidnightNavyBg
import com.example.ui.theme.MidnightNavyCanvasEnd
import com.example.ui.theme.MidnightNavyCanvasStart

/**
 * Exact ClarNote App Logo based on user specifications:
 * 1. Squircle container (radius ~28%) with Deep Space Midnight Navy gradient (#0F172A to #0B1120)
 * 2. Center Note Sheet with 45° Diagonal gradient (Electric Cyan #00D2FF -> Aqua Blue #00E5D4 -> Emerald Mint #10B981)
 * 3. Realistic Top-Right Page Curl (dog-ear fold) with inner shadow #059669 and ambient 3D depth
 * 4. Embedded sharp glowing 4-point Gemini star sparkle in top-left
 * 5. Modern typography: "ClarNote" with "Notes Organizer" subtitle
 */
@Composable
fun ClarNoteLogo(
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    showTypography: Boolean = false,
    subtitle: String = "Notes Organizer"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle_pulse")
    val sparkleScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleScale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Squircle App Icon Container
        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(percent = 28),
                    ambientColor = BrandElectricCyan.copy(alpha = 0.35f),
                    spotColor = BrandMintGreen.copy(alpha = 0.45f)
                )
                .clip(RoundedCornerShape(percent = 28))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF131F3B),
                            Color(0xFF0F172A),
                            Color(0xFF0B1120)
                        ),
                        center = Offset(0.35f, 0.25f),
                        radius = 280f
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            BrandElectricCyan.copy(alpha = 0.35f),
                            FrostedSlateBorder,
                            BrandMintGreen.copy(alpha = 0.25f)
                        )
                    ),
                    shape = RoundedCornerShape(percent = 28)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Note Sheet Canvas with 3D Page Curl & Gemini Sparkle
            Canvas(
                modifier = Modifier
                    .size(size * 0.58f)
            ) {
                val w = this.size.width
                val h = this.size.height
                val corner = w * 0.16f
                val foldSize = w * 0.30f

                // 1. Drop shadow behind the note sheet for realistic 3D depth
                val shadowPath = Path().apply {
                    moveTo(corner, 0f)
                    lineTo(w - foldSize, 0f)
                    lineTo(w, foldSize)
                    lineTo(w, h - corner)
                    quadraticTo(w, h, w - corner, h)
                    lineTo(corner, h)
                    quadraticTo(0f, h, 0f, h - corner)
                    lineTo(0f, corner)
                    quadraticTo(0f, 0f, corner, 0f)
                    close()
                }
                drawPath(
                    path = shadowPath,
                    color = Color.Black.copy(alpha = 0.40f)
                )

                // 2. Main Note Sheet with 45° Diagonal Brand Gradient
                val sheetPath = Path().apply {
                    moveTo(corner, 0f)
                    lineTo(w - foldSize, 0f)
                    lineTo(w, foldSize)
                    lineTo(w, h - corner)
                    quadraticTo(w, h, w - corner, h)
                    lineTo(corner, h)
                    quadraticTo(0f, h, 0f, h - corner)
                    lineTo(0f, corner)
                    quadraticTo(0f, 0f, corner, 0f)
                    close()
                }

                val sheetGradient = Brush.linearGradient(
                    colors = listOf(
                        BrandElectricCyan,
                        BrandAquaBlue,
                        BrandMintGreen
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )
                drawPath(path = sheetPath, brush = sheetGradient, style = Fill)

                // 3. Ambient Drop-Shadow under the Dog-Ear Fold
                val foldShadowPath = Path().apply {
                    moveTo(w - foldSize, 0f)
                    lineTo(w - foldSize, foldSize)
                    lineTo(w, foldSize)
                    close()
                }
                drawPath(
                    path = foldShadowPath,
                    color = Color(0xFF042B1D).copy(alpha = 0.55f)
                )

                // 4. Realistic Top-Right Dog-Ear Page Curl (curved inward fold)
                val flapPath = Path().apply {
                    moveTo(w - foldSize, 0f)
                    cubicTo(
                        w - foldSize, foldSize * 0.70f,
                        w - foldSize * 0.70f, foldSize,
                        w, foldSize
                    )
                    lineTo(w - foldSize, foldSize)
                    close()
                }

                val flapGradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF34D399),
                        BrandMintGreen,
                        BrandCornerFoldDark
                    ),
                    start = Offset(w - foldSize, 0f),
                    end = Offset(w, foldSize)
                )
                drawPath(path = flapPath, brush = flapGradient)

                // Flap edge highlight
                drawPath(
                    path = Path().apply {
                        moveTo(w - foldSize, 0f)
                        cubicTo(
                            w - foldSize, foldSize * 0.70f,
                            w - foldSize * 0.70f, foldSize,
                            w, foldSize
                        )
                    },
                    color = Color(0xFF6EE7B7).copy(alpha = 0.9f),
                    style = Stroke(width = 1.2f)
                )

                // 5. Embedded Sharp Glowing 4-Point Gemini Sparkle Star in Top-Left Area
                val starCenterX = w * 0.32f
                val starCenterY = h * 0.30f
                val starRadius = (w * 0.16f) * sparkleScale

                // Soft outer glow aura (#E0F7FA with 60% opacity)
                val outerAura = Path().apply {
                    moveTo(starCenterX, starCenterY - starRadius * 1.35f)
                    cubicTo(starCenterX, starCenterY, starCenterX, starCenterY, starCenterX + starRadius * 1.35f, starCenterY)
                    cubicTo(starCenterX, starCenterY, starCenterX, starCenterY, starCenterX, starCenterY + starRadius * 1.35f)
                    cubicTo(starCenterX, starCenterY, starCenterX, starCenterY, starCenterX - starRadius * 1.35f, starCenterY)
                    cubicTo(starCenterX, starCenterY, starCenterX, starCenterY, starCenterX, starCenterY - starRadius * 1.35f)
                    close()
                }
                drawPath(path = outerAura, color = BrandSparkleGlow)

                // Sharp bright white 4-pointed star
                val starPath = Path().apply {
                    moveTo(starCenterX, starCenterY - starRadius)
                    cubicTo(
                        starCenterX, starCenterY - starRadius * 0.25f,
                        starCenterX + starRadius * 0.25f, starCenterY,
                        starCenterX + starRadius, starCenterY
                    )
                    cubicTo(
                        starCenterX + starRadius * 0.25f, starCenterY,
                        starCenterX, starCenterY + starRadius * 0.25f,
                        starCenterX, starCenterY + starRadius
                    )
                    cubicTo(
                        starCenterX, starCenterY + starRadius * 0.25f,
                        starCenterX - starRadius * 0.25f, starCenterY,
                        starCenterX - starRadius, starCenterY
                    )
                    cubicTo(
                        starCenterX - starRadius * 0.25f, starCenterY,
                        starCenterX, starCenterY - starRadius * 0.25f,
                        starCenterX, starCenterY - starRadius
                    )
                    close()
                }
                drawPath(path = starPath, color = Color.White)
            }
        }

        // Typography: "ClarNote" with "Notes Organizer" subtitle
        if (showTypography) {
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ClarNote",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClarNoteSparkleIcon(size = 14.dp, tint = BrandElectricCyan)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.6.sp
            )
        }
    }
}

/**
 * Universal 4-pointed Gemini Sparkle Icon from the ClarNote logo.
 * Used across the app for:
 * - Top header branding dot above 'C'
 * - Bottom navigation center dock trigger
 * - AI response bullet points & formula highlights
 */
@Composable
fun ClarNoteSparkleIcon(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = BrandElectricCyan,
    outerGlowTint: Color = BrandSparkleGlow,
    withOuterGlow: Boolean = true
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = w / 2f

        if (withOuterGlow) {
            val aura = Path().apply {
                moveTo(cx, 0f)
                cubicTo(cx, cy, cx, cy, w, cy)
                cubicTo(cx, cy, cx, cy, cx, h)
                cubicTo(cx, cy, cx, cy, 0f, cy)
                cubicTo(cx, cy, cx, cy, cx, 0f)
                close()
            }
            drawPath(path = aura, color = outerGlowTint)
        }

        val star = Path().apply {
            moveTo(cx, cy - r * 0.85f)
            cubicTo(cx, cy - r * 0.22f, cx + r * 0.22f, cy, cx + r * 0.85f, cy)
            cubicTo(cx + r * 0.22f, cy, cx, cy + r * 0.22f, cx, cy + r * 0.85f)
            cubicTo(cx, cy + r * 0.22f, cx - r * 0.22f, cy, cx - r * 0.85f, cy)
            cubicTo(cx - r * 0.22f, cy, cx, cy - r * 0.22f, cx, cy - r * 0.85f)
            close()
        }
        drawPath(path = star, color = tint)

        // Bright white center pinpoint
        drawCircle(
            color = Color.White,
            radius = r * 0.20f,
            center = Offset(cx, cy)
        )
    }
}

/**
 * Dog-Ear Page-Fold Card Shape for Subject Folders:
 * Features the signature curved corner fold from the ClarNote logo!
 */
@Composable
fun LogoThemedDogEarFolderCard(
    modifier: Modifier = Modifier,
    subjectName: String,
    fileCount: Int,
    accentColor: Color,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .background(FrostedSlateSurface.copy(alpha = 0.78f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.65f),
                        FrostedSlateBorder.copy(alpha = 0.50f),
                        accentColor.copy(alpha = 0.20f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .shadow(6.dp, RoundedCornerShape(18.dp), ambientColor = accentColor.copy(alpha = 0.2f))
    ) {
        // Subtle corner page-fold graphic in the card's top-right corner
        Canvas(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopEnd)
        ) {
            val w = this.size.width
            val h = this.size.height

            val foldPath = Path().apply {
                moveTo(0f, 0f)
                cubicTo(0f, h * 0.75f, w * 0.25f, h, w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = foldPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.6f),
                        BrandCornerFoldDark.copy(alpha = 0.7f)
                    )
                )
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.16f))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                // Mint green badge for file count (e.g. "12 files")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandMintGreen.copy(alpha = 0.15f))
                        .border(1.dp, BrandMintGreen.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$fileCount files",
                        color = BrandMintGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = subjectName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                ClarNoteSparkleIcon(size = 11.dp, tint = accentColor, withOuterGlow = false)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AI Study Notes",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}
