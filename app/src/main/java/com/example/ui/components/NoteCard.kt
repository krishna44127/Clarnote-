package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.data.model.NoteType
import com.example.data.model.UploadStatus
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.DangerRose
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

@Composable
fun NoteCard(
    note: NoteItem,
    onClick: () -> Unit,
    onToggleStar: () -> Unit,
    onDeleteDuplicate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = note.uploadProgress,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "upload_progress"
    )

    val typeIcon: ImageVector = when (note.type) {
        NoteType.PDF -> Icons.Default.PictureAsPdf
        NoteType.IMAGE -> Icons.Default.Image
        NoteType.VOICE -> Icons.Default.Audiotrack
        NoteType.TEXT -> Icons.Default.Description
        NoteType.VIDEO -> Icons.Default.Movie
    }

    val typeBadgeColor = when (note.type) {
        NoteType.PDF -> Color(0xFFDC2626)
        NoteType.IMAGE -> Color(0xFF0284C7)
        NoteType.VOICE -> Color(0xFF8B5CF6)
        NoteType.TEXT -> Color(0xFF10B981)
        NoteType.VIDEO -> Color(0xFFEA580C)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("note_card_${note.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Optimistic Zero-Delay Upload Status Bar
            AnimatedVisibility(visible = note.uploadStatus == UploadStatus.UPLOADING) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Uploading chunk silently (${(animatedProgress * 100).toInt()}%)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Optimistic UI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape),
                        color = PrimaryBlue,
                        trackColor = PrimaryBlue.copy(alpha = 0.2f)
                    )
                }
            }

            // 1. Hash-Based Duplicate File Detected Banner
            if (note.isDuplicate) {
                Surface(
                    color = DangerRose.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, DangerRose.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Duplicate Warning",
                                tint = DangerRose,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Duplicate File Detected!",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = DangerRose,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Matches exact SHA-256 hash of '${note.duplicateOfTitle ?: "Original File"}'. Keep space clean?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onDeleteDuplicate,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DangerRose
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("cleanup_duplicate_button_${note.id}")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("1-Click Duplicate Cleanup", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 2. Perceptual Near-Duplicate Alert Banner
            if (note.isNearDuplicate && !note.isDuplicate) {
                Surface(
                    color = AccentAmber.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, AccentAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WarningAmber,
                            contentDescription = "Near-Duplicate Warning",
                            tint = AccentAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = note.nearDuplicateReason ?: "Near-Duplicate: High visual similarity. Version with higher contrast detected.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Header: Type Badge, Subject, Star, Mistake Diary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Type Badge
                    Surface(
                        color = typeBadgeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = typeIcon,
                                contentDescription = null,
                                tint = typeBadgeColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = note.type.name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = typeBadgeColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Subject Pill
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = note.subject,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (note.pagesCount > 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${note.pagesCount} pgs",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (note.isMistakeDiary) {
                        Surface(
                            color = Color(0xFFF43F5E).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "Mistake Diary",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFF43F5E),
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleStar,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("star_button_${note.id}")
                    ) {
                        Icon(
                            imageVector = if (note.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star Note",
                            tint = if (note.isStarred) AccentAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Content snippet
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Topic tags & Compression space saved badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Topic Tags
                Text(
                    text = note.tags,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Compression Space Saved Badge
                val origMb = String.format("%.1f", note.originalSizeBytes / (1024.0 * 1024.0))
                val compMb = String.format("%.1f", note.compressedSizeBytes / (1024.0 * 1024.0))
                Surface(
                    color = SuccessEmerald.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "💾 -68% ($compMb MB)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SuccessEmerald,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Revision Reminder indicator if set
            if (note.revisionIntervalDays > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Revision Reminder",
                        tint = AccentAmber,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Spaced revision set (${note.revisionIntervalDays}d cycle)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}
