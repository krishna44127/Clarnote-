package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.ui.components.NoteCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

@Composable
fun SearchScreen(
    notes: List<NoteItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNoteClick: (NoteItem) -> Unit,
    onToggleStar: (NoteItem) -> Unit,
    onDeleteDuplicate: (NoteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickTerms = listOf("Lens formula", "Snell", "Carnot", "Mitosis", "Calculus", "Integration", "Electrodynamics", "Gauss")

    val matchingNotes = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        notes.filter { note ->
            note.title.contains(searchQuery, ignoreCase = true) ||
            note.content.contains(searchQuery, ignoreCase = true) ||
            note.ocrText.contains(searchQuery, ignoreCase = true) ||
            note.tags.contains(searchQuery, ignoreCase = true) ||
            note.subject.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("search_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Search Header & Input
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Deep OCR & Content Search",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Instantly finds text inside scanned images, handwriting OCR, PDFs & audio memos",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search any word, formula, or scanned diagram...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryBlue)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deep_ocr_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Quick OCR & Study Query Tags",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickTerms) { term ->
                        FilterChip(
                            selected = searchQuery.equals(term, ignoreCase = true),
                            onClick = { onSearchQueryChange(if (searchQuery == term) "" else term) },
                            label = { Text(term, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // Result Status
        if (searchQuery.isNotBlank()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Found ${matchingNotes.size} ${if (matchingNotes.size == 1) "result" else "results"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Surface(
                        color = SuccessEmerald.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Full OCR Deep Scan Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessEmerald,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Empty state
        if (searchQuery.isNotBlank() && matchingNotes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matches found for '$searchQuery'",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else if (searchQuery.isBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "💡 How ClarNote Deep OCR Search Works:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• Scanned Lecture Sheets: ClarNote extracts text and mathematical symbols from your photo and PDF pages.\n• Handwriting Recognition: Key formulas like Gauss's Law, Snell's Law, and integrals are indexed.\n• Voice Memos: Spoken words transcribed by the microphone are searchable instantly.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Result items
        items(matchingNotes, key = { it.id }) { note ->
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
