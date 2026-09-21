package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.ui.components.NoteCard
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.DangerRose
import com.example.ui.theme.PrimaryBlue

@Composable
fun RecentStarredScreen(
    activeNotes: List<NoteItem>,
    starredNotes: List<NoteItem>,
    mistakeDiaryNotes: List<NoteItem>,
    subFilter: Int, // 0 = Recent, 1 = VIP Starred, 2 = Mistake Diary
    onSelectSubFilter: (Int) -> Unit,
    onNoteClick: (NoteItem) -> Unit,
    onToggleStar: (NoteItem) -> Unit,
    onDeleteDuplicate: (NoteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayNotes = when (subFilter) {
        1 -> starredNotes
        2 -> mistakeDiaryNotes
        else -> activeNotes.sortedByDescending { it.createdAt }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("recent_starred_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Sub-Filter Tabs
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = when (subFilter) {
                        1 -> "Starred Notes Hub"
                        2 -> "Mistake Diary"
                        else -> "Recent Study Notes"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = when (subFilter) {
                        1 -> "Fast access to critical formulas and high-priority revision notes"
                        2 -> "Curated collection of tough doubts and tricky questions"
                        else -> "Chronological stream of your study captures"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = subFilter == 0,
                        onClick = { onSelectSubFilter(0) },
                        label = { Text("Recent", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.testTag("filter_recent")
                    )

                    FilterChip(
                        selected = subFilter == 1,
                        onClick = { onSelectSubFilter(1) },
                        label = { Text("Starred (${starredNotes.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentAmber.copy(alpha = 0.2f),
                            selectedLabelColor = AccentAmber
                        ),
                        modifier = Modifier.testTag("filter_starred")
                    )

                    FilterChip(
                        selected = subFilter == 2,
                        onClick = { onSelectSubFilter(2) },
                        label = { Text("Mistake Diary (${mistakeDiaryNotes.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = DangerRose, modifier = Modifier.size(16.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DangerRose.copy(alpha = 0.2f),
                            selectedLabelColor = DangerRose
                        ),
                        modifier = Modifier.testTag("filter_mistake_diary")
                    )
                }
            }
        }

        // Empty state
        if (displayNotes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = when (subFilter) {
                                1 -> Icons.Default.Star
                                2 -> Icons.Default.PriorityHigh
                                else -> Icons.Default.BookmarkBorder
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (subFilter) {
                                1 -> "No starred notes yet. Tap the star ⭐ on any note!"
                                2 -> "No tricky questions marked in Mistake Diary yet."
                                else -> "No recent notes found."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Notes List
        items(displayNotes, key = { it.id }) { note ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note) },
                    onToggleStar = { onToggleStar(note) },
                    onDeleteDuplicate = { onDeleteDuplicate(note) }
                )
            }
        }
    }
}
