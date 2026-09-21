package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.NoteItem
import com.example.ui.GoogleCloudAccount
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.DangerRose
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.NeonMint
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.SunsetCoral

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    storageLocation: String,
    onSetStorageLocation: (String) -> Unit,
    onClearCache: () -> Unit,
    googleAccountEmail: String,
    onSwitchGoogleAccount: (String) -> Unit,
    connectedAccounts: List<GoogleCloudAccount> = emptyList(),
    onAddGoogleAccount: (String) -> Unit = {},
    onRemoveGoogleAccount: (String) -> Unit = {},
    onSetActiveBackupAccount: (String) -> Unit = {},
    autoPoolStorageEnabled: Boolean = true,
    onToggleAutoPoolStorage: (Boolean) -> Unit = {},
    syncOnlyWifi: Boolean,
    onToggleSyncOnlyWifi: (Boolean) -> Unit,
    autoSyncFrequency: String,
    onSetAutoSyncFrequency: (String) -> Unit,
    onTriggerSync: () -> Unit,
    isSyncing: Boolean,
    geminiDefaultModel: String,
    onSetGeminiModel: (String) -> Unit,
    geminiResponseTone: String,
    onSetResponseTone: (String) -> Unit,
    appTheme: String,
    onSetAppTheme: (String) -> Unit,
    accentColor: String,
    onSetAccentColor: (String) -> Unit,
    customWallpaperUri: String? = null,
    onSetCustomWallpaperUri: (String?) -> Unit = {},
    wallpaperBlur: Float = 0f,
    onSetWallpaperBlur: (Float) -> Unit = {},
    wallpaperDarkness: Float = 0.3f,
    onSetWallpaperDarkness: (Float) -> Unit = {},
    biometricLockEnabled: Boolean,
    onToggleBiometricLock: (Boolean) -> Unit,
    trashRetentionDays: Int,
    onSetTrashRetentionDays: (Int) -> Unit,
    trashNotes: List<NoteItem>,
    onRestoreFromTrash: (Long) -> Unit,
    onEmptyTrash: () -> Unit,
    onOpenTrashBin: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var showAddAccountDialog by remember { mutableStateOf(false) }
    var newAccountInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("settings_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "ClarNote storage, cloud architecture & AI configurations",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ================= SECTION 1: STORAGE & DIRECTORY =================
            SectionHeader(title = "1. Storage & Directory", icon = Icons.Filled.Storage, color = ElectricCyan)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Storage Location Path",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Choose where original study documents and scanned notes are stored on your device.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val storageOptions = listOf("App Private Directory", "Device Internal (/Documents/ClarNote/)", "External SD Card")
                    storageOptions.forEach { opt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onSetStorageLocation(opt) }
                                .padding(vertical = 6.dp, horizontal = 4.dp)
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = storageLocation == opt,
                                onClick = { onSetStorageLocation(opt) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (storageLocation == opt) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cache cleaner
                    OutlinedButton(
                        onClick = onClearCache,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("clear_cache_button")
                    ) {
                        Icon(imageVector = Icons.Filled.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear Image Cache & RAM (Frees ~42.8 MB)")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= SECTION 2: GOOGLE DRIVE CLOUD SYNC & MULTI-ACCOUNT HUB =================
            SectionHeader(title = "2. Google Drive Cloud Hub (Multi-Account)", icon = Icons.Filled.CloudDone, color = SuccessEmerald)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Connected Google Cloud Accounts",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Zero private server fees. Each linked Google account gives +15 GB free cloud storage for your lecture notes & PDFs.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val accountsToDisplay = if (connectedAccounts.isNotEmpty()) connectedAccounts else listOf(
                        GoogleCloudAccount(email = googleAccountEmail, usedBytes = 1_400_000_000L, isActiveBackup = true)
                    )

                    accountsToDisplay.forEach { acc ->
                        val usedGb = String.format(java.util.Locale.US, "%.1f", acc.usedBytes.toDouble() / (1024.0 * 1024.0 * 1024.0))
                        val totalGb = String.format(java.util.Locale.US, "%.0f", acc.totalBytes.toDouble() / (1024.0 * 1024.0 * 1024.0))
                        val progress = (acc.usedBytes.toFloat() / acc.totalBytes.toFloat()).coerceIn(0.01f, 1f)

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (acc.isActiveBackup) SuccessEmerald.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (acc.isActiveBackup) SuccessEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Icon(
                                            imageVector = Icons.Filled.AccountCircle,
                                            contentDescription = null,
                                            tint = if (acc.isActiveBackup) SuccessEmerald else PrimaryBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = acc.email,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                maxLines = 1
                                            )
                                            if (acc.isActiveBackup) {
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = SuccessEmerald.copy(alpha = 0.2f),
                                                    modifier = Modifier.padding(top = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "● ACTIVE BACKUP TARGET",
                                                        color = SuccessEmerald,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (!acc.isActiveBackup) {
                                            OutlinedButton(
                                                onClick = { onSetActiveBackupAccount(acc.email) },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(32.dp),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("Make Active", fontSize = 11.sp)
                                            }
                                        }
                                        if (accountsToDisplay.size > 1) {
                                            IconButton(
                                                onClick = { onRemoveGoogleAccount(acc.email) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.RemoveCircleOutline,
                                                    contentDescription = "Unlink",
                                                    tint = DangerRose,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "$usedGb GB of $totalGb GB used",
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                    Text(
                                        text = "${(progress * 100).toInt()}%",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SuccessEmerald)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = if (progress > 0.85f) DangerRose else SuccessEmerald
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Add Another Google Account Button
                    Button(
                        onClick = {
                            newAccountInput = ""
                            showAddAccountDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_google_account_button")
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("+ Add Another Google Account (+15 GB Free)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Smart Auto-Pool Storage Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(imageVector = Icons.Filled.AutoMode, contentDescription = null, tint = BrandElectricCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Smart Multi-Account Auto-Pool",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Auto-switches to next linked account when 90% cloud quota is reached for infinite free storage.",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                        Switch(
                            checked = autoPoolStorageEnabled,
                            onCheckedChange = onToggleAutoPoolStorage,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BrandElectricCyan)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Wifi only toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Wifi, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Sync Only on Wi-Fi", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text(text = "Saves mobile data", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            }
                        }
                        Switch(
                            checked = syncOnlyWifi,
                            onCheckedChange = onToggleSyncOnlyWifi,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SuccessEmerald)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Auto sync frequency
                    Text(text = "Auto-Sync Frequency", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Instant", "Daily", "Off").forEach { freq ->
                            FilterChip(
                                selected = autoSyncFrequency == freq,
                                onClick = { onSetAutoSyncFrequency(freq) },
                                label = { Text(freq) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SuccessEmerald.copy(alpha = 0.2f),
                                    selectedLabelColor = SuccessEmerald
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onTriggerSync,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("sync_now_button")
                    ) {
                        Icon(imageVector = Icons.Filled.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isSyncing) "Syncing with Google Drive..." else "Sync Now to Google Drive")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= SECTION 3: GEMINI AI STUDIO ENGINE =================
            SectionHeader(title = "3. Gemini AI Studio Engine", icon = Icons.Filled.Psychology, color = RoyalIndigo)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Default Model", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(
                        text = "Choose which Gemini model powers your study copilot.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        FilterChip(
                            selected = geminiDefaultModel == "Gemini 1.5 Flash",
                            onClick = { onSetGeminiModel("Gemini 1.5 Flash") },
                            label = { Text("⚡ 1.5 Flash (Fast)") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = geminiDefaultModel == "Gemini 1.5 Pro",
                            onClick = { onSetGeminiModel("Gemini 1.5 Pro") },
                            label = { Text("🧠 1.5 Pro (Deep)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Response Tone", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Concise", "Detailed", "Step-by-Step").forEach { tone ->
                            FilterChip(
                                selected = geminiResponseTone.startsWith(tone),
                                onClick = { onSetResponseTone(tone) },
                                label = { Text(tone) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= SECTION 4: THEME & VISUAL CUSTOMIZATION =================
            SectionHeader(title = "4. Theme & Visual Customization", icon = Icons.Filled.Palette, color = ElectricCyan)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Live Mini-Phone Mockup Preview
                    Text(
                        text = "Live Theme & Wallpaper Preview",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val chosenAccentColor = when (accentColor) {
                        "Nordic Mint" -> NeonMint
                        "Royal Indigo" -> RoyalIndigo
                        "Sunset Coral" -> SunsetCoral
                        else -> ElectricCyan
                    }
                    val previewBgColor = when (appTheme) {
                        "Deep Space Midnight Navy" -> Color(0xFF0B1120)
                        "True AMOLED Black" -> Color(0xFF000000)
                        "Clean Paper White" -> Color(0xFFF8FAFC)
                        else -> Color(0xFF0F172A)
                    }
                    val previewCardColor = when (appTheme) {
                        "Deep Space Midnight Navy" -> Color(0xFF131D2E)
                        "True AMOLED Black" -> Color(0xFF18181B)
                        "Clean Paper White" -> Color(0xFFFFFFFF)
                        else -> Color(0xFF1E293B)
                    }
                    val previewTextColor = when (appTheme) {
                        "Clean Paper White" -> Color(0xFF0F172A)
                        else -> Color(0xFFF8FAFC)
                    }

                    // Centered Mini-Phone Mockup
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .width(170.dp)
                                .height(220.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            color = previewBgColor,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(2.5.dp, Color(0xFF475569))
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                // Wallpaper overlay simulation if applied
                                if (customWallpaperUri != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                                    listOf(
                                                        Color(0xFF3B82F6).copy(alpha = 0.5f),
                                                        Color(0xFF8B5CF6).copy(alpha = 0.5f)
                                                    )
                                                )
                                            )
                                    )
                                    // Darkness dim overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = wallpaperDarkness))
                                    )
                                }

                                // Mini Phone UI Shell
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    // Mini Status bar & Camera Notch
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(36.dp)
                                                .height(6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF334155))
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Mini App Title
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "ClarNote",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = previewTextColor
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(chosenAccentColor)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Mini 2-Column Folders
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = previewCardColor,
                                            modifier = Modifier.weight(1f).height(64.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(6.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clip(CircleShape)
                                                        .background(chosenAccentColor.copy(alpha = 0.25f))
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("Physics", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = previewTextColor)
                                                Text("3 notes", fontSize = 6.sp, color = previewTextColor.copy(alpha = 0.6f))
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = previewCardColor,
                                            modifier = Modifier.weight(1f).height(64.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(6.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clip(CircleShape)
                                                        .background(chosenAccentColor.copy(alpha = 0.25f))
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("Maths", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = previewTextColor)
                                                Text("5 notes", fontSize = 6.sp, color = previewTextColor.copy(alpha = 0.6f))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    // Mini bottom FAB
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(chosenAccentColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("+", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Theme Canvas", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        listOf("Deep Space Midnight Navy", "Dark Graphite", "True AMOLED Black", "Clean Paper White").forEach { theme ->
                            FilterChip(
                                selected = appTheme == theme,
                                onClick = { onSetAppTheme(theme) },
                                label = { Text(theme, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Accent Color", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        listOf("Electric Cyan" to ElectricCyan, "Nordic Mint" to NeonMint, "Royal Indigo" to RoyalIndigo, "Sunset Coral" to SunsetCoral).forEach { (name, col) ->
                            FilterChip(
                                selected = accentColor == name,
                                onClick = { onSetAccentColor(name) },
                                leadingIcon = {
                                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(col))
                                },
                                label = { Text(name, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gallery Wallpaper Picker via Photo Picker
                    val wallpaperPickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia()
                    ) { uri ->
                        if (uri != null) {
                            onSetCustomWallpaperUri(uri.toString())
                        }
                    }

                    Text(
                        text = "Custom Wallpaper & Ambiance",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                wallpaperPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Wallpaper", fontSize = 12.sp)
                        }

                        if (customWallpaperUri != null) {
                            OutlinedButton(
                                onClick = { onSetCustomWallpaperUri(null) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Remove", fontSize = 12.sp)
                            }
                        }
                    }

                    // Blur & Darkness Sliders
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Wallpaper Blur: ${(wallpaperBlur * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = wallpaperBlur,
                        onValueChange = onSetWallpaperBlur,
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Darkness / Dimming: ${(wallpaperDarkness * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = wallpaperDarkness,
                        onValueChange = onSetWallpaperDarkness,
                        valueRange = 0f..0.85f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= SECTION 5: SECURITY & PRIVACY =================
            SectionHeader(title = "5. Security & Privacy", icon = Icons.Filled.Security, color = DangerRose)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Biometric Lock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Fingerprint, contentDescription = null, tint = DangerRose, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Biometric App Lock", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text(text = "Fingerprint / Face Unlock", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            }
                        }
                        Switch(
                            checked = biometricLockEnabled,
                            onCheckedChange = onToggleBiometricLock,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = DangerRose)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Trash retention
                    Text(text = "30-Day Trash Bin Retention", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = "Deleted files remain recoverable before permanent erasure.", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(7, 15, 30).forEach { days ->
                            FilterChip(
                                selected = trashRetentionDays == days,
                                onClick = { onSetTrashRetentionDays(days) },
                                label = { Text("$days Days") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onOpenTrashBin,
                        colors = ButtonDefaults.buttonColors(containerColor = FrostedSlateSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedSlateBorder),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_trash_bin_button")
                    ) {
                        Icon(imageVector = Icons.Filled.DeleteSweep, contentDescription = null, tint = DangerRose, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Full Trash Bin (${trashNotes.size} notes)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    if (trashNotes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Deleted Items",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            TextButton(onClick = onEmptyTrash) {
                                Text("Empty All", color = DangerRose, fontSize = 12.sp)
                            }
                        }

                        trashNotes.take(3).forEach { note ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = note.title, maxLines = 1, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                IconButton(onClick = { onRestoreFromTrash(note.id) }, modifier = Modifier.size(28.dp)) {
                                    Icon(imageVector = Icons.Filled.Restore, contentDescription = "Restore", tint = SuccessEmerald, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Add Google Account Dialog
    if (showAddAccountDialog) {
        AlertDialog(
            onDismissRequest = { showAddAccountDialog = false },
            title = { Text("Add Google Drive Account") },
            text = {
                Column {
                    Text(
                        text = "ClarNote links multiple Google accounts to pool free 15 GB cloud storage slots together with zero server fees.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newAccountInput,
                        onValueChange = { newAccountInput = it },
                        label = { Text("Enter Google Email") },
                        placeholder = { Text("student.secondary@gmail.com") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAccountInput.isNotBlank()) {
                            onAddGoogleAccount(newAccountInput.trim())
                            showAddAccountDialog = false
                        }
                    }
                ) {
                    Text("Add Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.15f))
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}
