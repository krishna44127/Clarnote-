package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BadgeCameraBg
import com.example.ui.theme.BadgeCameraIcon
import com.example.ui.theme.BadgeCanvasBg
import com.example.ui.theme.BadgeCanvasIcon
import com.example.ui.theme.BadgeDocsBg
import com.example.ui.theme.BadgeDocsIcon
import com.example.ui.theme.BadgeFolderBg
import com.example.ui.theme.BadgeFolderIcon
import com.example.ui.theme.BadgeGalleryBg
import com.example.ui.theme.BadgeGalleryIcon
import com.example.ui.theme.BadgeScannerBg
import com.example.ui.theme.BadgeScannerIcon
import com.example.ui.theme.BrandAquaBlue
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.BrandMintGreen
import com.example.ui.theme.ClarNoteBrandGradient
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.MidnightNavyBg
import com.example.ui.theme.PrimaryBlue

@Composable
fun FloatingQuickActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(ClarNoteBrandGradient)
            .clickable(onClick = onClick)
            .testTag("floating_quick_action_fab"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Quick Action (+)",
            tint = Color(0xFF0B101D),
            modifier = Modifier.size(30.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionMenuSheet(
    onDismiss: () -> Unit,
    onOpenCreateFolder: () -> Unit,
    onOpenScanner: () -> Unit,
    onOpenPdfMerger: () -> Unit,
    onOpenVoiceMemo: () -> Unit,
    onOpenPdfImport: () -> Unit,
    onOpenScratchpad: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = FrostedSlateSurface,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("quick_action_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClarNoteSparkleIcon(size = 18.dp, tint = BrandElectricCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ClarNote Quick Actions (+)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Canvas & Scratchpad: Soft Violet Glow (#818CF8 on #1E1B4B)
            QuickActionItem(
                icon = Icons.Default.Create,
                iconColor = BadgeCanvasIcon,
                iconBgColor = BadgeCanvasBg,
                title = "Canvas & Handwriting Scratchpad",
                description = "Freeform pen drawing, rough derivation, equations & formulas",
                onClick = {
                    onDismiss()
                    onOpenScratchpad()
                }
            )

            // 2. Documents & PDF Import: Coral Red Badge (#F87171 on #450A0A)
            QuickActionItem(
                icon = Icons.Default.Description,
                iconColor = BadgeDocsIcon,
                iconBgColor = BadgeDocsBg,
                title = "Documents & PDF Import",
                description = "Import lecture PDFs, slides & heavy study material with 68% compression",
                onClick = {
                    onDismiss()
                    onOpenPdfImport()
                }
            )

            // 3. Instant Camera Capture: Sky Blue Badge (#38BDF8 on #0C4A6E)
            QuickActionItem(
                icon = Icons.Default.CameraAlt,
                iconColor = BadgeCameraIcon,
                iconBgColor = BadgeCameraBg,
                title = "Instant Camera Capture",
                description = "Snap whiteboard or notebook page with auto-perspective adjustment",
                onClick = {
                    onDismiss()
                    onOpenScanner()
                }
            )

            // 4. Document Scanner: Electric Cyan Badge (#06B6D4 on #164E63)
            QuickActionItem(
                icon = Icons.Default.DocumentScanner,
                iconColor = BadgeScannerIcon,
                iconBgColor = BadgeScannerBg,
                title = "Document Scanner",
                description = "High-contrast crystal-clear scanner with automated edge cropping",
                onClick = {
                    onDismiss()
                    onOpenScanner()
                }
            )

            // 5. Gallery & Photos to PDF: Mint Green Badge (#34D399 on #064E3B)
            QuickActionItem(
                icon = Icons.Default.PhotoLibrary,
                iconColor = BadgeGalleryIcon,
                iconBgColor = BadgeGalleryBg,
                title = "Gallery & Photos to PDF",
                description = "Pick photos from device or Google Photos to arrange into 1 crisp PDF",
                onClick = {
                    onDismiss()
                    onOpenPdfMerger()
                }
            )

            // 6. New Subject Folder: Warm Amber Badge (#FBBF24 on #451A03)
            QuickActionItem(
                icon = Icons.Default.Folder,
                iconColor = BadgeFolderIcon,
                iconBgColor = BadgeFolderBg,
                title = "New Subject Folder",
                description = "Create a custom subject category (e.g. Organic Chem, Modern Physics)",
                onClick = {
                    onDismiss()
                    onOpenCreateFolder()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color = iconColor.copy(alpha = 0.16f),
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = FrostedSlateSurface, // Dark Slate Glass (#131D2E)
        border = BorderStroke(1.dp, FrostedSlateBorder), // 1px subtle border (#1E2B45)
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vibrant Icon Badge in soft glowing container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor)
                    .border(1.dp, iconColor.copy(alpha = 0.35f * pulseAlpha), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                // Title Text: Pure Crisp White (#FFFFFF), SemiBold (600), 15sp
                Text(
                    text = title,
                    color = Color(0xFFFFFFFF),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Subtitle Text: Soft Cyan-Slate (#94A3B8), Regular (400), 12sp
                Text(
                    text = description,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScratchpadDialog(
    onDismiss: () -> Unit,
    onSaveNote: (title: String, content: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Quick Text Scratchpad",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Note Title (e.g. Wave Optics PYQ)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note Content / Formulas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                placeholder = { Text("Type important formulas or lecture summaries...") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        onSaveNote(title, content)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Note (Auto-Categorized & Compressed)")
            }
        }
    }
}
