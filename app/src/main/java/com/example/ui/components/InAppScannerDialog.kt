package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteType
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppScannerDialog(
    onDismiss: () -> Unit,
    onSaveScan: (title: String, content: String, ocrText: String, subject: String?) -> Unit
) {
    var docTitle by remember { mutableStateOf("Scanned Lecture Sheet") }
    var selectedFilter by remember { mutableStateOf("Crisp B&W") }
    var rotationDegrees by remember { mutableStateOf(0) }
    var isEdgeCropActive by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("in_app_scanner_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "In-App Document Auto-Scanner",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Auto-edge crop & high-contrast clarity filter",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close Scanner")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Viewfinder Simulated Scan Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, PrimaryBlue.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Background Document Simulation
                val pageBgColor = when (selectedFilter) {
                    "Crisp B&W" -> Color.White
                    "High Contrast" -> Color(0xFFE2E8F0)
                    else -> Color(0xFFFEF3C7)
                }

                Surface(
                    color = pageBgColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CHAPTER 04: ELECTRODYNAMICS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Gauss's Law Formula:\n∮ E · dA = Q_enclosed / ε₀\nElectric flux through closed surface is proportional to enclosed charge.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E293B),
                            lineHeight = 16.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Page 1/1", fontSize = 10.sp, color = Color.Gray)
                            Text("Auto-Enhanced (Crystal Clear)", fontSize = 10.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Edge Detection Crop Overlays
                if (isEdgeCropActive) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 3.dp.toPx()
                        val cornerLen = 20.dp.toPx()
                        val pad = 18.dp.toPx()

                        // Top-Left
                        drawLine(PrimaryBlue, Offset(pad, pad), Offset(pad + cornerLen, pad), stroke)
                        drawLine(PrimaryBlue, Offset(pad, pad), Offset(pad, pad + cornerLen), stroke)

                        // Top-Right
                        drawLine(PrimaryBlue, Offset(size.width - pad, pad), Offset(size.width - pad - cornerLen, pad), stroke)
                        drawLine(PrimaryBlue, Offset(size.width - pad, pad), Offset(size.width - pad, pad + cornerLen), stroke)

                        // Bottom-Left
                        drawLine(PrimaryBlue, Offset(pad, size.height - pad), Offset(pad + cornerLen, size.height - pad), stroke)
                        drawLine(PrimaryBlue, Offset(pad, size.height - pad), Offset(pad, size.height - pad - cornerLen), stroke)

                        // Bottom-Right
                        drawLine(PrimaryBlue, Offset(size.width - pad, size.height - pad), Offset(size.width - pad - cornerLen, size.height - pad), stroke)
                        drawLine(PrimaryBlue, Offset(size.width - pad, size.height - pad), Offset(size.width - pad, size.height - pad - cornerLen), stroke)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Selector & Rotation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Crisp B&W", "High Contrast", "Original").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 11.sp) }
                        )
                    }
                }

                Row {
                    IconButton(onClick = { rotationDegrees = (rotationDegrees + 90) % 360 }) {
                        Icon(Icons.Default.RotateRight, contentDescription = "Rotate")
                    }
                    IconButton(onClick = { isEdgeCropActive = !isEdgeCropActive }) {
                        Icon(
                            Icons.Default.Crop,
                            contentDescription = "Auto Crop",
                            tint = if (isEdgeCropActive) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = docTitle,
                onValueChange = { docTitle = it },
                label = { Text("Note Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scanner_title_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val ocr = "Gauss's law: closed integral E · dA = Q_enclosed / epsilon_0. Flux through closed gaussian surface is charge / epsilon_0. Chapter 4 Electrodynamics."
                    val content = "Crystal-clear scan of Chapter 04 Electrodynamics: Gauss's Law derivation, spherical symmetry application, and point charge field calculation."
                    onSaveScan(docTitle, content, ocr, "Physics")
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_scanned_doc_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Crystal-Clear Document & Run OCR", fontWeight = FontWeight.Bold)
            }
        }
    }
}
