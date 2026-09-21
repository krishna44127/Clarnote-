package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteItem
import com.example.ui.SubjectItem
import com.example.ui.components.ClarNoteLogo
import com.example.ui.components.ClarNoteSparkleIcon
import com.example.ui.theme.BrandAquaBlue
import com.example.ui.theme.BrandCornerFoldDark
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.BrandMintGreen
import com.example.ui.theme.ClarNoteBrandGradient
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.MidnightNavyBg
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    notes: List<NoteItem>,
    subjects: List<SubjectItem>,
    pinnedNotes: List<NoteItem>,
    isSyncing: Boolean,
    onFolderClick: (String) -> Unit,
    onNoteClick: (NoteItem) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onFabClick: () -> Unit,
    onOpenGeminiStudio: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentHour = remember {
        java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    }
    val greeting = remember(currentHour) {
        when (currentHour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..21 -> "Good evening"
            else -> "Good evening"
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Redesigned Header Bar matching Logo Theme:
            // Top Left: "ClarNote" in crisp white, with a small cyan sparkle dot over the 'C'
            // Top Right: Search and Settings in frosted navy circular buttons (#131C2E with #1E2B45 border)
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.padding(vertical = 4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Cyan Sparkle Dot floating above the 'C'
                        ClarNoteSparkleIcon(
                            size = 11.dp,
                            tint = BrandElectricCyan,
                            modifier = Modifier
                                .offset(x = 1.dp, y = (-12).dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ClarNote",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = (-0.3).sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Next-Gen Glowing Gemini AI Sparkle Orb (Universal 4-point sparkle)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FrostedSlateSurface)
                            .border(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(BrandElectricCyan, BrandAquaBlue, BrandMintGreen)
                                ),
                                CircleShape
                            )
                            .clickable { onOpenGeminiStudio() }
                            .testTag("home_gemini_orb")
                    ) {
                        ClarNoteSparkleIcon(
                            size = 18.dp,
                            tint = BrandElectricCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Frosted Navy Circular Search Button (#131C2E, #1E2B45 border)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FrostedSlateSurface)
                            .border(1.dp, FrostedSlateBorder, CircleShape)
                            .clickable { onSearchClick() }
                            .testTag("home_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Notes",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Frosted Navy Circular Settings Button (#131C2E, #1E2B45 border)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FrostedSlateSurface)
                            .border(1.dp, FrostedSlateBorder, CircleShape)
                            .clickable { onSettingsClick() }
                            .testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MidnightNavyBg
                )
            )

            // Dynamic Greeting Area (Large typography with subtle cyan-mint underline/glow)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$greeting,",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF94A3B8),
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = "Welcome to ClarNote",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                )

                // Subtle signature cyan-mint glow underline
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(ClarNoteBrandGradient)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main View: 2-Column Subject Folder Grid or Empty State Placeholder
            if (subjects.isEmpty()) {
                // Empty State: ClarNote Logo Vector Illustration radiating gentle cyan light
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                        .testTag("home_empty_placeholder")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // ClarNote Logo radiating gentle cyan ambient aura
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            // Gentle radiating background glow
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                BrandElectricCyan.copy(alpha = 0.22f),
                                                BrandAquaBlue.copy(alpha = 0.10f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                            ClarNoteLogo(
                                size = 96.dp,
                                showTypography = false
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "No Subject Folders Yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = (-0.3).sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tap '+' to create your first subject folder, scan notebook pages, or start drawing on the canvas.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF94A3B8),
                                lineHeight = 21.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Signature Brand Gradient Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(ClarNoteBrandGradient)
                                .clickable { onFabClick() }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color(0xFF0B101D),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Create First Subject Folder",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0B101D),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("home_folder_grid"),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Pinned Vault Strip if available
                    if (pinnedNotes.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            QuickVaultPinnedStrip(
                                pinnedNotes = pinnedNotes,
                                onNoteClick = onNoteClick
                            )
                        }
                    }

                    // Section Header: Subject Folders
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ClarNoteSparkleIcon(size = 14.dp, tint = BrandElectricCyan, withOuterGlow = false)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Subject Folders",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                            Text(
                                text = "${subjects.size} subjects",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF94A3B8)
                                )
                            )
                        }
                    }

                    // 2-Column Subject Folder Cards with Dog-Ear Page Fold & Color Accents
                    items(subjects) { subject ->
                        val fileCount = notes.count { it.subject.equals(subject.name, ignoreCase = true) }
                        LogoThemedSubjectFolderCard(
                            subject = subject,
                            fileCount = fileCount,
                            onClick = { onFolderClick(subject.name) }
                        )
                    }
                }
            }
        }

        // Floating (+) Action Button with full Cyan-to-Mint gradient
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 88.dp, end = 20.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    ambientColor = BrandElectricCyan,
                    spotColor = BrandMintGreen
                )
                .clip(CircleShape)
                .background(ClarNoteBrandGradient)
                .clickable { onFabClick() }
                .size(56.dp)
                .testTag("home_fab_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Folder or Upload",
                tint = Color(0xFF0B101D),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// 2-Column Subject Folder Card with Logo-Themed Dog-Ear Page-Fold & Color Accent Outlines
@Composable
private fun LogoThemedSubjectFolderCard(
    subject: SubjectItem,
    fileCount: Int,
    onClick: () -> Unit
) {
    // Specific accent colors per user prompt:
    // Physics: Cyan outline (#00D2FF)
    // Chemistry: Mint Green outline (#10B981)
    // Biology: Emerald Aqua outline (#00E5D4)
    val accentColor = when (subject.name.lowercase().trim()) {
        "physics" -> BrandElectricCyan
        "chemistry" -> BrandMintGreen
        "biology" -> BrandAquaBlue
        "mathematics", "maths" -> Color(0xFFF59E0B)
        "computer science", "cs" -> Color(0xFF8B5CF6)
        else -> Color(subject.colorHex)
    }

    val iconVector = when (subject.name.lowercase().trim()) {
        "physics" -> Icons.Default.Science
        "chemistry" -> Icons.Default.Biotech
        "biology" -> Icons.Default.Biotech
        "mathematics", "maths" -> Icons.Default.Calculate
        "computer science", "cs" -> Icons.Default.Computer
        else -> Icons.Default.Folder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(FrostedSlateSurface.copy(alpha = 0.85f))
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        accentColor.copy(alpha = 0.85f),
                        FrostedSlateBorder,
                        accentColor.copy(alpha = 0.35f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .testTag("folder_card_${subject.name}")
    ) {
        // Logo-themed dog-ear page curl in the top-right corner
        Canvas(
            modifier = Modifier
                .size(34.dp)
                .align(Alignment.TopEnd)
        ) {
            val w = this.size.width
            val h = this.size.height

            // Realistic inward fold
            val foldPath = Path().apply {
                moveTo(0f, 0f)
                cubicTo(0f, h * 0.70f, w * 0.30f, h, w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = foldPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.75f),
                        BrandCornerFoldDark.copy(alpha = 0.90f)
                    )
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Subject Icon in colored rounded container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor.copy(alpha = 0.16f))
                        .border(1.dp, accentColor.copy(alpha = 0.40f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = subject.name,
                        tint = accentColor,
                        modifier = Modifier.size(23.dp)
                    )
                }

                // Mint-Green Badge with file count (e.g. "12 files")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandMintGreen.copy(alpha = 0.15f))
                        .border(1.dp, BrandMintGreen.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$fileCount ${if (fileCount == 1) "file" else "files"}",
                        color = BrandMintGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Folder Name
            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle with mini sparkle
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClarNoteSparkleIcon(size = 11.dp, tint = accentColor, withOuterGlow = false)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AI Study Vault",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

// Slim single-row Pinned Notes Horizontal Slider (Quick Vault Strip)
@Composable
private fun QuickVaultPinnedStrip(
    pinnedNotes: List<NoteItem>,
    onNoteClick: (NoteItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                tint = BrandElectricCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Pinned Strip • Quick Vault",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        if (pinnedNotes.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = FrostedSlateSurface.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    FrostedSlateBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClarNoteSparkleIcon(size = 16.dp, tint = BrandElectricCyan)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Star notes to pin your top formulas & daily targets here for instant access",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(pinnedNotes.take(6), key = { it.id }) { note ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = FrostedSlateSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            BrandElectricCyan.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onNoteClick(note) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(BrandElectricCyan.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = null,
                                    tint = BrandElectricCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = note.subject,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BrandMintGreen,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

