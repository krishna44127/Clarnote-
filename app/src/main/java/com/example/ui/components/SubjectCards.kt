package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.SubjectItem

@Composable
fun SubjectCardsSection(
    subjects: List<SubjectItem>,
    selectedSubject: String?,
    notes: List<NoteItem>,
    onSelectSubject: (String?) -> Unit,
    onAddFolderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subject Folders",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )

            if (selectedSubject != null) {
                Text(
                    text = "Clear Filter",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .clickable { onSelectSubject(null) }
                        .padding(4.dp)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // "All Notes" Card
            item {
                val isSelected = selectedSubject == null
                val totalNotesCount = notes.size
                SubjectCard(
                    title = "All Notes",
                    count = totalNotesCount,
                    color = Color(0xFF64748B),
                    icon = Icons.Default.Folder,
                    isSelected = isSelected,
                    onClick = { onSelectSubject(null) }
                )
            }

            // Subject Cards
            items(subjects) { subject ->
                val isSelected = selectedSubject == subject.name
                val count = notes.count { it.subject.equals(subject.name, ignoreCase = true) }
                val icon = when (subject.name.lowercase()) {
                    "physics" -> Icons.Default.Science
                    "chemistry" -> Icons.Default.Science
                    "biology" -> Icons.Default.Science
                    "mathematics" -> Icons.Default.Calculate
                    "computer science" -> Icons.Default.Computer
                    else -> Icons.Default.Folder
                }

                SubjectCard(
                    title = subject.name,
                    count = count,
                    color = Color(subject.colorHex),
                    icon = icon,
                    isSelected = isSelected,
                    onClick = { onSelectSubject(subject.name) }
                )
            }

            // Add Custom Folder Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .width(108.dp)
                        .height(118.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onAddFolderClick() }
                        .testTag("add_folder_card"),
                    tonalElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Folder",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "New Folder",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    title: String,
    count: Int,
    color: Color,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) color else Color.Transparent,
        label = "border_color"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = if (isSelected) 0.22f else 0.12f),
        modifier = Modifier
            .width(118.dp)
            .height(118.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("subject_card_${title.lowercase()}"),
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1
                )
                Text(
                    text = "$count ${if (count == 1) "note" else "notes"}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun TopicTagsBar(
    availableTags: List<String>,
    selectedTag: String?,
    onSelectTag: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultTags = listOf("#Optics", "#Formulas", "#PyQ", "#Thermodynamics", "#Organic", "#Genetics", "#Calculus", "#KeyNotes")
    val combinedTags = (defaultTags + availableTags).distinct()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedTag == null,
                onClick = { onSelectTag(null) },
                label = { Text("All Tags", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("filter_tag_all")
            )
        }

        items(combinedTags) { tag ->
            val isSelected = selectedTag == tag
            FilterChip(
                selected = isSelected,
                onClick = { onSelectTag(if (isSelected) null else tag) },
                label = { Text(tag, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag("filter_tag_${tag.replace("#", "")}")
            )
        }
    }
}
