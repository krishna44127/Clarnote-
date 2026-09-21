package com.example.ui.screens

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.PrimaryBlue

@Composable
fun PermissionPrimerScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRequesting by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Proceed once user responds
        onContinue()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBg)
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .testTag("permission_primer_screen")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Modern glowing icon orb
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(PrimaryBlue.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )
                        .border(
                            1.5.dp,
                            Brush.linearGradient(listOf(ElectricCyan, PrimaryBlue)),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.FolderSpecial,
                        contentDescription = "Safe Storage",
                        tint = ElectricCyan,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome to ClarNote",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your Private, Intelligent Digital Study Locker",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = ElectricCyan,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ClarNote needs access to your files to organize your study notes, PDFs, and lecture recordings safely on your device.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF94A3B8),
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 3 Privacy & Storage Value Props Cards
                PermissionValuePropCard(
                    icon = Icons.Filled.Shield,
                    iconTint = NeonMint,
                    title = "100% Offline & Private",
                    description = "Your notes and assignments stay securely encrypted on your device. Zero unwanted third-party data tracking."
                )

                Spacer(modifier = Modifier.height(12.dp))

                PermissionValuePropCard(
                    icon = Icons.Filled.Security,
                    iconTint = ElectricCyan,
                    title = "Direct Document Organization",
                    description = "Seamlessly organize WhatsApp study PDFs, scanned assignments, and lecture audio into 2-column subject folders."
                )

                Spacer(modifier = Modifier.height(12.dp))

                PermissionValuePropCard(
                    icon = Icons.Filled.CheckCircle,
                    iconTint = PrimaryBlue,
                    title = "One-Time Setup",
                    description = "ClarNote will only request this once. We respect your peace of mind and study focus."
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                Button(
                    onClick = {
                        isRequesting = true
                        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            arrayOf(
                                android.Manifest.permission.READ_MEDIA_IMAGES,
                                android.Manifest.permission.READ_MEDIA_VIDEO,
                                android.Manifest.permission.READ_MEDIA_AUDIO,
                                android.Manifest.permission.CAMERA
                            )
                        } else {
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA
                            )
                        }
                        permissionLauncher.launch(permissionsToRequest)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("grant_permission_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Grant Access & Continue",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Storage access is used exclusively for study notes & documents.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PermissionValuePropCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131828)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF94A3B8),
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
