package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.data.model.NoteType
import com.example.ui.components.NoteCard
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FolderDetailScreen(
    folderName: String,
    notes: List<NoteItem>,
    availableFolders: List<String>,
    selectedTag: String?,
    selectedType: NoteType?,
    selectedNoteIds: Set<Long>,
    onSelectTag: (String?) -> Unit,
    onSelectType: (NoteType?) -> Unit,
    onBack: () -> Unit,
    onNoteClick: (NoteItem) -> Unit,
    onToggleStar: (NoteItem) -> Unit,
    onDeleteDuplicate: (NoteItem) -> Unit,
    onToggleSelection: (Long) -> Unit,
    onSelectAll: (List<NoteItem>) -> Unit,
    onClearSelection: () -> Unit,
    onPinSelected: () -> Unit,
    onMoveSelected: (String) -> Unit,
    onDeleteSelected: () -> Unit,
    onBatchUpload: (count: Int) -> Unit,
    onOpenGeminiCopilot: () -> Unit,
    onPickSystemFiles: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showBatchUploadDialog by remember { mutableStateOf(false) }
    var showMoveFolderDialog by remember { mutableStateOf(false) }

    val folderNotes = notes.filter { it.subject.equals(folderName, ignoreCase = true) }
    val filteredNotes = folderNotes.filter { note ->
        val matchTag = selectedTag == null || note.tags.contains(selectedTag, ignoreCase = true)
        val matchType = selectedType == null || note.type == selectedType
        matchTag && matchType
    }

    val isMultiSelectActive = selectedNoteIds.isNotEmpty()
    val allSelected = filteredNotes.isNotEmpty() && selectedNoteIds.size >= filteredNotes.size

    val availableTags = remember(folderNotes) {
        folderNotes.flatMap { it.tags.split(",") }
            .map { it.trim() }
            .filter { it.startsWith("#") }
            .distinct()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isMultiSelectActive) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${selectedNoteIds.size} Selected",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    } else {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = folderName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = PrimaryBlue.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "${folderNotes.size} files",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryBlue
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (isMultiSelectActive) {
                        IconButton(onClick = onClearSelection) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Selection"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("folder_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Folders"
                            )
                        }
                    }
                },
                actions = {
                    if (isMultiSelectActive) {
                        // Select All Checkbox button
                        IconButton(
                            onClick = { onSelectAll(filteredNotes) },
                            modifier = Modifier.testTag("select_all_button")
                        ) {
                            Icon(
                                imageVector = if (allSelected) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                contentDescription = "Select All",
                                tint = PrimaryBlue
                            )
                        }
                    } else {
                        // Multi-Select Mode Toggle Button (☑️)
                        if (filteredNotes.isNotEmpty()) {
                            IconButton(
                                onClick = { onToggleSelection(filteredNotes.first().id) },
                                modifier = Modifier.testTag("folder_multi_select_toggle_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Checklist,
                                    contentDescription = "Multi-Select Mode",
                                    tint = PrimaryBlue
                                )
                            }
                        }

                        // Batch Multi-Upload Button (+10, +20, +50 files)
                        IconButton(
                            onClick = { showBatchUploadDialog = true },
                            modifier = Modifier.testTag("folder_batch_upload_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = "Multi-Upload Batch",
                                tint = PrimaryBlue
                            )
                        }

                        // Dedicated Google Gemini Study Copilot Button
                        IconButton(
                            onClick = onOpenGeminiCopilot,
                            modifier = Modifier.testTag("folder_gemini_sparkle_button")
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryBlue.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Gemini Study Copilot",
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Bulk Actions Bar (Sticky at Bottom when 1 or more items selected)
            AnimatedVisibility(
                visible = isMultiSelectActive,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bulk_actions_bar"),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pin to Top
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).combinedClickable(onClick = onPinSelected).padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pin to Top",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Pin to Top", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Move to Folder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).combinedClickable(onClick = { showMoveFolderDialog = true }).padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DriveFileMove,
                                contentDescription = "Move to Folder",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Move", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Delete Selected
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).combinedClickable(onClick = onDeleteSelected).padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Selected",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Trash", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
                        }

                        // Share
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).combinedClickable(onClick = {
                                val selectedNotes = folderNotes.filter { it.id in selectedNoteIds }
                                val shareText = selectedNotes.joinToString("\n\n---\n\n") { "${it.title}\n${it.content}" }
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Selected Notes"))
                            }).padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Share", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("folder_detail_list"),
            contentPadding = PaddingValues(bottom = if (isMultiSelectActive) 80.dp else 24.dp)
        ) {
            // Folder Inside Filter Chips Bar (Shifted inside folder, not on home!)
            item {
                Column(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                    // Topic Tags Bar (#Optics, #Formulas, #PyQ)
                    if (availableTags.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedTag == null,
                                    onClick = { onSelectTag(null) },
                                    label = { Text("All Tags", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue.copy(alpha = 0.18f),
                                        selectedLabelColor = PrimaryBlue
                                    )
                                )
                            }
                            items(availableTags) { tag ->
                                FilterChip(
                                    selected = selectedTag == tag,
                                    onClick = { onSelectTag(if (selectedTag == tag) null else tag) },
                                    label = { Text(tag, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue.copy(alpha = 0.18f),
                                        selectedLabelColor = PrimaryBlue
                                    )
                                )
                            }
                        }
                    }

                    // Media Type Chips (PDF, Images, Voice, Videos)
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedType == null,
                                onClick = { onSelectType(null) },
                                label = { Text("All Formats", fontSize = 11.sp) }
                            )
                        }
                        items(NoteType.values()) { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { onSelectType(if (selectedType == type) null else type) },
                                label = { Text(type.name, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            // Quick Multi-Upload Action Strip banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${filteredNotes.size} ${if (filteredNotes.size == 1) "document" else "documents"}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        TextButton(
                            onClick = { showBatchUploadDialog = true },
                            modifier = Modifier.testTag("batch_upload_strip_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Batch Upload", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Empty state
            if (filteredNotes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No notes in $folderName yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = { showBatchUploadDialog = true },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Upload Notes to $folderName")
                            }
                        }
                    }
                }
            }

            // Note items list
            items(filteredNotes, key = { it.id }) { note ->
                val isSelected = note.id in selectedNoteIds

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .combinedClickable(
                            onClick = {
                                if (isMultiSelectActive) {
                                    onToggleSelection(note.id)
                                } else {
                                    onNoteClick(note)
                                }
                            },
                            onLongClick = {
                                onToggleSelection(note.id)
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isMultiSelectActive) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onToggleSelection(note.id) },
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            NoteCard(
                                note = note,
                                onClick = {
                                    if (isMultiSelectActive) {
                                        onToggleSelection(note.id)
                                    } else {
                                        onNoteClick(note)
                                    }
                                },
                                onToggleStar = { onToggleStar(note) },
                                onDeleteDuplicate = { onDeleteDuplicate(note) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Batch Multi-Upload Dialog (10, 20, 50 files in 1 click)
    if (showBatchUploadDialog) {
        AlertDialog(
            onDismissRequest = { showBatchUploadDialog = false },
            title = {
                Text(
                    text = "Batch Multi-Upload",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select files from gallery or file manager to import simultaneously into $folderName:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showBatchUploadDialog = false
                            onPickSystemFiles()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.fillMaxWidth().testTag("folder_browse_storage_btn")
                    ) {
                        Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select from Storage / Gallery (SAF)")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Or instant simulated batch test:", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onBatchUpload(10)
                                showBatchUploadDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("10 Files", fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                onBatchUpload(20)
                                showBatchUploadDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("20 Files", fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                onBatchUpload(50)
                                showBatchUploadDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("50 Files", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBatchUploadDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Move to Folder Dialog
    if (showMoveFolderDialog) {
        AlertDialog(
            onDismissRequest = { showMoveFolderDialog = false },
            title = { Text("Move ${selectedNoteIds.size} Notes To") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableFolders.forEach { targetFolder ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (targetFolder == folderName) PrimaryBlue.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .combinedClickable {
                                    onMoveSelected(targetFolder)
                                    showMoveFolderDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = PrimaryBlue)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(targetFolder, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showMoveFolderDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
