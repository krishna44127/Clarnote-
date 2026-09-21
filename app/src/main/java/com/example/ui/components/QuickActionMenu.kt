package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FloatingQuickActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
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
            modifier = Modifier.size(28.dp)
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
    onOpenScratchpad: () -> Unit,
    onOpenGalleryImport: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = FrostedSlateSurface, // Dark Slate Glass #131D2E
        scrimColor = Color.Black.copy(alpha = 0.65f),
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
                    ClarNoteSparkleIcon(size = 20.dp, tint = BrandElectricCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Let's get started",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Side-by-side Top Cards: Canvas & Documents
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(84.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismiss()
                            onOpenScratchpad()
                        },
                    color = BadgeCanvasBg,
                    border = BorderStroke(1.dp, BadgeCanvasIcon.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Create,
                            contentDescription = null,
                            tint = BadgeCanvasIcon,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Canvas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(84.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onDismiss()
                            onOpenPdfImport()
                        },
                    color = BadgeDocsBg,
                    border = BorderStroke(1.dp, BadgeDocsIcon.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = BadgeDocsIcon,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Documents", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("PDF, DOC, PPT...", color = Color(0xFF94A3B8), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action List Items
            QuickActionItem(
                icon = Icons.Default.CameraAlt,
                iconColor = BadgeCameraIcon,
                iconBgColor = BadgeCameraBg,
                title = "Take Photo",
                description = "Capture a photo instantly",
                onClick = {
                    onDismiss()
                    onOpenScanner()
                }
            )

            QuickActionItem(
                icon = Icons.Default.DocumentScanner,
                iconColor = BadgeScannerIcon,
                iconBgColor = BadgeScannerBg,
                title = "Scan",
                description = "Scan papers and documents",
                onClick = {
                    onDismiss()
                    onOpenScanner()
                }
            )

            QuickActionItem(
                icon = Icons.Default.PhotoLibrary,
                iconColor = BadgeGalleryIcon,
                iconBgColor = BadgeGalleryBg,
                title = "Gallery",
                description = "Add photo from your gallery",
                onClick = {
                    onDismiss()
                    onOpenGalleryImport()
                }
            )

            HorizontalDivider(
                color = FrostedSlateBorder,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            QuickActionItem(
                icon = Icons.Default.Folder,
                iconColor = BadgeFolderIcon,
                iconBgColor = BadgeFolderBg,
                title = "Folder",
                description = "Create a new subject folder",
                onClick = {
                    onDismiss()
                    onOpenCreateFolder()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = FrostedSlateSurface,
        border = BorderStroke(1.dp, FrostedSlateBorder),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBgColor)
                    .border(1.dp, iconColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = description,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
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
