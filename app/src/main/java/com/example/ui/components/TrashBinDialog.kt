package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NoteItem
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.DangerRose
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.NeonMint
import com.example.ui.theme.SuccessEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashBinDialog(
    trashNotes: List<NoteItem>,
    onRestoreNote: (Long) -> Unit,
    onPermanentDeleteNote: (Long) -> Unit,
    onRestoreSelected: (Set<Long>) -> Unit,
    onPermanentDeleteSelected: (Set<Long>) -> Unit,
    onEmptyAllTrash: () -> Unit,
    onDismiss: () -> Unit
) {
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedIds = remember { mutableStateListOf<Long>() }
    var showConfirmDeleteAllDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteBatchDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSelectionMode) {
                            Text(
                                text = "${selectedIds.size} Selected",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandElectricCyan
                                )
                            )
                        } else {
                            Column {
                                Text(
                                    text = "Trash Bin",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "${trashNotes.size} deleted items (30-day recovery)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (isSelectionMode) {
                            IconButton(onClick = {
                                isSelectionMode = false
                                selectedIds.clear()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Selection")
                            }
                        } else {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    actions = {
                        if (isSelectionMode) {
                            // Select / Deselect All
                            val allSelected = trashNotes.isNotEmpty() && selectedIds.size == trashNotes.size
                            IconButton(onClick = {
                                if (allSelected) {
                                    selectedIds.clear()
                                } else {
                                    selectedIds.clear()
                                    selectedIds.addAll(trashNotes.map { it.id })
                                }
                            }) {
                                Icon(
                                    imageVector = if (allSelected) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                    contentDescription = "Select All",
                                    tint = BrandElectricCyan
                                )
                            }
                        } else {
                            // Permanent Multi-Select icon (☑️)
                            if (trashNotes.isNotEmpty()) {
                                IconButton(
                                    onClick = { isSelectionMode = true },
                                    modifier = Modifier.testTag("trash_multi_select_toggle")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Checklist,
                                        contentDescription = "Multi-Select",
                                        tint = BrandElectricCyan
                                    )
                                }
                                IconButton(onClick = { showConfirmDeleteAllDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteSweep,
                                        contentDescription = "Empty Trash",
                                        tint = DangerRose
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (trashNotes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Trash Bin is Empty",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Deleted notes will safely appear here for 30 days.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 12.dp,
                            bottom = if (isSelectionMode && selectedIds.isNotEmpty()) 100.dp else 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(trashNotes, key = { it.id }) { note ->
                            val isSelected = selectedIds.contains(note.id)
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) FrostedSlateSurface.copy(alpha = 0.9f) else FrostedSlateSurface
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) BrandElectricCyan else FrostedSlateBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isSelectionMode) {
                                            if (isSelected) selectedIds.remove(note.id)
                                            else selectedIds.add(note.id)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelectionMode) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (isSelected) BrandElectricCyan else Color(0xFF64748B),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFF1E293B)
                                            ) {
                                                Text(
                                                    text = note.subject,
                                                    fontSize = 10.sp,
                                                    color = BrandElectricCyan,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "30-day retention active",
                                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8))
                                            )
                                        }
                                    }

                                    if (!isSelectionMode) {
                                        IconButton(
                                            onClick = { onRestoreNote(note.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Restore,
                                                contentDescription = "Restore",
                                                tint = SuccessEmerald,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { onPermanentDeleteNote(note.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteForever,
                                                contentDescription = "Delete Forever",
                                                tint = DangerRose,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Floating Batch Action Dock for Trash
                AnimatedVisibility(
                    visible = isSelectionMode && selectedIds.isNotEmpty(),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = FrostedSlateSurface,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BrandElectricCyan.copy(alpha = 0.5f)),
                        shadowElevation = 12.dp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    onRestoreSelected(selectedIds.toSet())
                                    selectedIds.clear()
                                    isSelectionMode = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Restore (${selectedIds.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { showConfirmDeleteBatchDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = DangerRose),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Delete (${selectedIds.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for Empty All
    if (showConfirmDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteAllDialog = false },
            title = { Text("Empty Entire Trash Bin?", fontWeight = FontWeight.Bold) },
            text = { Text("All ${trashNotes.size} notes in Trash will be permanently erased. This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onEmptyAllTrash()
                        showConfirmDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRose)
                ) {
                    Text("Empty All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeleteAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Confirmation dialog for Batch Delete
    if (showConfirmDeleteBatchDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteBatchDialog = false },
            title = { Text("Permanently Delete ${selectedIds.size} Notes?", fontWeight = FontWeight.Bold) },
            text = { Text("These items will be permanently erased from device storage and cannot be restored.") },
            confirmButton = {
                Button(
                    onClick = {
                        onPermanentDeleteSelected(selectedIds.toSet())
                        selectedIds.clear()
                        isSelectionMode = false
                        showConfirmDeleteBatchDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRose)
                ) {
                    Text("Delete Forever")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeleteBatchDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
