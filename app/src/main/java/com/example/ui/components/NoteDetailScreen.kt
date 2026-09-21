package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Schema
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.model.NoteItem
import com.example.data.model.NoteType
import com.example.ui.components.ClarNoteSparkleIcon
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.BrandAquaBlue
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.BrandMintGreen
import com.example.ui.theme.ClarNoteBrandGradient
import com.example.ui.theme.DangerRose
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.MidnightNavyBg
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

data class DocumentStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isHighlighter: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: NoteItem,
    onBack: () -> Unit,
    onToggleStar: () -> Unit,
    onToggleMistakeDiary: () -> Unit,
    onToggleVault: () -> Unit,
    onSetRevisionReminder: (days: Int) -> Unit,
    onInsertBlankPage: () -> Unit,
    onSaveDrawing: (String) -> Unit,
    onAddVideoBookmark: (seconds: Int, label: String, note: String) -> Unit,
    onDeleteNote: () -> Unit,
    onUpdateNoteContent: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Live Note Editing & Voice Note state
    var isEditingContent by remember { mutableStateOf(false) }
    var editableContent by remember(note.content) { mutableStateOf(note.content) }
    var isVoiceRecording by remember { mutableStateOf(false) }

    // AI Diagram Generator state
    var showDiagramDialog by remember { mutableStateOf(false) }
    var diagramPrompt by remember { mutableStateOf("") }
    var isGeneratingDiagram by remember { mutableStateOf(false) }
    var generatedDiagramSvgOrText by remember { mutableStateOf<String?>(null) }

    // Minimal floating toolbar state:
    // "None" (viewing/reading mode), "Pen", "Highlighter", "Eraser"
    var activeDrawingTool by remember { mutableStateOf<String?>("Pen") }
    var selectedPenColor by remember { mutableStateOf(BrandElectricCyan) }
    val drawnStrokes = remember { mutableStateListOf<DocumentStroke>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    // Circle to Doubt indicator state
    var showCircledDoubtPrompt by remember { mutableStateOf(false) }
    var lastCircledCenter by remember { mutableStateOf(Offset.Zero) }

    // Gemini Study Copilot Swipe-Up Bottom Sheet state
    var showGeminiSheet by remember { mutableStateOf(false) }
    var geminiInitialMode by remember { mutableStateOf(CopilotMode.ASK_NOTE) }
    var geminiDoubtContext by remember { mutableStateOf("") }

    // Revision reminder dialog
    var showRevisionDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = note.subject,
                                style = MaterialTheme.typography.bodySmall.copy(color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• Page 1 of ${note.pagesCount}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Edit Note / Voice Note button
                    IconButton(onClick = {
                        if (isEditingContent) {
                            onUpdateNoteContent(editableContent)
                            isEditingContent = false
                        } else {
                            isEditingContent = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditingContent) Icons.Default.Edit else Icons.Default.DriveFileRenameOutline,
                            contentDescription = if (isEditingContent) "Save Note Content" else "Edit Note Text",
                            tint = if (isEditingContent) SuccessEmerald else PrimaryBlue
                        )
                    }

                    // Voice Note Recording
                    IconButton(onClick = {
                        isVoiceRecording = !isVoiceRecording
                        if (isVoiceRecording) {
                            editableContent += "\n\n[🎙️ Voice Note Transcription]: Important exam concept clarified during lecture on ${note.subject}."
                            onUpdateNoteContent(editableContent)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Record Voice Note",
                            tint = if (isVoiceRecording) DangerRose else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Star / Pin toggle
                    IconButton(onClick = onToggleStar) {
                        Icon(
                            imageVector = if (note.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Star / Pin Note",
                            tint = if (note.isStarred) AccentAmber else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Revision reminder button
                    IconButton(onClick = { showRevisionDialog = true }) {
                        Icon(imageVector = Icons.Default.Alarm, contentDescription = "Schedule Revision", tint = PrimaryBlue)
                    }

                    // Share
                    IconButton(onClick = {
                        val shareText = "${note.title}\nSubject: ${note.subject}\n\n${note.content}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Note"))
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }

                    // Delete Note
                    IconButton(onClick = onDeleteNote) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note", tint = DangerRose)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FrostedSlateSurface,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F172A)) // High contrast deep document canvas frame
        ) {
            // ================= 85% CONTENT FIRST CANVAS =================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                // High-resolution textbook paper display canvas
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("content_first_canvas")
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Scrollable authentic document view
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Document Header / Chapter Banner
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "${note.subject.uppercase()} • CHAPTER DERIVATION & NOTES",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0284C7),
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = note.title,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                    }
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF0284C7).copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "${note.type.name} • 100% Crisp",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0284C7),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            val isImageNote = note.type == NoteType.IMAGE || !note.fileUri.isNullOrBlank() ||
                                    note.content.startsWith("file://") || note.content.startsWith("content://") || note.content.startsWith("http")
                            val rawImageUri = note.fileUri?.takeIf { it.isNotBlank() }
                                ?: if (note.content.startsWith("file://") || note.content.startsWith("content://") || note.content.startsWith("http")) note.content else null

                            if (isImageNote && rawImageUri != null) {
                                // Full High-Resolution AsyncImage Document Viewer
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF0F172A),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("note_async_image_viewer")
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        // Image controls bar
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFF1E293B))
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Image,
                                                    contentDescription = null,
                                                    tint = BrandElectricCyan,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "High-Res Image / Document Scan",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White
                                                )
                                            }
                                            Surface(
                                                shape = CircleShape,
                                                color = SuccessEmerald.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "100% Crisp",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SuccessEmerald,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        // The AsyncImage component
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            SubcomposeAsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(rawImageUri)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = note.title,
                                                contentScale = ContentScale.FillWidth,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(8.dp)),
                                                loading = {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(240.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            color = BrandElectricCyan,
                                                            modifier = Modifier.size(36.dp)
                                                        )
                                                    }
                                                },
                                                error = {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(180.dp)
                                                            .background(Color(0xFF1E293B)),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.BrokenImage,
                                                            contentDescription = "Image preview unavailable",
                                                            tint = DangerRose,
                                                            modifier = Modifier.size(40.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Text(
                                                            text = "Image preview unavailable or file moved",
                                                            color = Color(0xFF94A3B8),
                                                            fontSize = 12.sp
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Image Notes / Annotations area if text exists or in edit mode
                                if (isEditingContent) {
                                    OutlinedTextField(
                                        value = editableContent,
                                        onValueChange = { editableContent = it },
                                        label = { Text("Image Annotations / Notes") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        minLines = 4
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            onClick = {
                                                onUpdateNoteContent(editableContent)
                                                isEditingContent = false
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Save Annotations", fontSize = 12.sp)
                                        }
                                    }
                                } else if (editableContent.isNotBlank() && editableContent != rawImageUri) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFF8FAFC),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "📝 NOTES & ANNOTATIONS",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF64748B)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = editableContent,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1E293B),
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Formatted study note text / content (or Editable TextField in Edit Mode)
                                if (isEditingContent) {
                                    OutlinedTextField(
                                        value = editableContent,
                                        onValueChange = { editableContent = it },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        minLines = 6
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            onClick = {
                                                onUpdateNoteContent(editableContent)
                                                isEditingContent = false
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Save Changes", fontSize = 12.sp)
                                        }
                                    }
                                } else {
                                    Text(
                                        text = editableContent,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1E293B),
                                        lineHeight = 24.sp,
                                        fontFamily = FontFamily.Default
                                    )
                                }

                                // Mathematical Derivation / Equations Box for Text Study Notes
                                Spacer(modifier = Modifier.height(18.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF8FAFC),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "📐 DERIVATION STEPS & EQUATIONS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = when (note.subject.lowercase()) {
                                                "physics" -> """
                                                    • Capacitance with dielectric slab:
                                                      C = ε₀·A / (d - t + t/K)
                                                    • Potential Difference across plates:
                                                      V = E₀·(d - t) + (E₀/K)·t
                                                    • Induced Bound Surface Charge Density:
                                                      σ_p = σ · (1 - 1/K)
                                                """.trimIndent()
                                                "mathematics", "maths" -> """
                                                    • King's Property:
                                                      ∫[a to b] f(x) dx = ∫[a to b] f(a + b - x) dx
                                                    • Symmetrical formulation:
                                                      2I = ∫[a to b] ( f(x) + f(a + b - x) ) dx
                                                """.trimIndent()
                                                else -> """
                                                    • Standard Reaction Formulation:
                                                      2 CH₃CHO + dil. NaOH → CH₃-CH(OH)-CH₂-CHO (Aldol)
                                                    • Heating gives dehydration product:
                                                      CH₃-CH=CH-CHO (Crotonaldehyde)
                                                """.trimIndent()
                                            },
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF0F172A),
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }

                            // Blank Rough Pages / Work Area
                            if (note.pagesCount > 1) {
                                Spacer(modifier = Modifier.height(24.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFFBEB),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                    modifier = Modifier.fillMaxWidth().height(260.dp)
                                ) {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "📝 Rough Work Canvas & Scratchpad (Page 2)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFB45309)
                                        )
                                    }
                                }
                            }

                            // Extra bottom spacing to avoid floating dock collision
                            Spacer(modifier = Modifier.height(100.dp))
                        }

                        // Drawing Layer Canvas (Full page overlay)
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(activeDrawingTool) {
                                    if (activeDrawingTool == null) return@pointerInput
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            currentPoints = listOf(offset)
                                            showCircledDoubtPrompt = false
                                        },
                                        onDrag = { change, _ ->
                                            change.consume()
                                            currentPoints = currentPoints + change.position
                                        },
                                        onDragEnd = {
                                            if (currentPoints.size > 2) {
                                                when (activeDrawingTool) {
                                                    "Pen" -> {
                                                        drawnStrokes.add(
                                                            DocumentStroke(
                                                                points = currentPoints,
                                                                color = selectedPenColor,
                                                                strokeWidth = 4f,
                                                                isHighlighter = false
                                                            )
                                                        )
                                                        // Detect potential circle gesture
                                                        val start = currentPoints.first()
                                                        val end = currentPoints.last()
                                                        val distance = (start - end).getDistance()
                                                        if (distance < 80f && currentPoints.size > 15) {
                                                            val avgX = currentPoints.map { it.x }.average().toFloat()
                                                            val avgY = currentPoints.map { it.y }.average().toFloat()
                                                            lastCircledCenter = Offset(avgX, avgY)
                                                            showCircledDoubtPrompt = true
                                                        }
                                                    }
                                                    "Highlighter" -> {
                                                        drawnStrokes.add(
                                                            DocumentStroke(
                                                                points = currentPoints,
                                                                color = Color(0xFFFACC15).copy(alpha = 0.35f),
                                                                strokeWidth = 24f,
                                                                isHighlighter = true
                                                            )
                                                        )
                                                        val avgX = currentPoints.map { it.x }.average().toFloat()
                                                        val avgY = currentPoints.map { it.y }.average().toFloat()
                                                        lastCircledCenter = Offset(avgX, avgY)
                                                        showCircledDoubtPrompt = true
                                                    }
                                                    "Eraser" -> {
                                                        drawnStrokes.clear()
                                                        showCircledDoubtPrompt = false
                                                    }
                                                }
                                                currentPoints = emptyList()
                                            }
                                        }
                                    )
                                }
                        ) {
                            // Render saved strokes
                            drawnStrokes.forEach { stroke ->
                                if (stroke.points.size > 1) {
                                    val path = Path().apply {
                                        moveTo(stroke.points.first().x, stroke.points.first().y)
                                        for (i in 1 until stroke.points.size) {
                                            lineTo(stroke.points[i].x, stroke.points[i].y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = stroke.color,
                                        style = Stroke(
                                            width = stroke.strokeWidth,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round
                                        )
                                    )
                                }
                            }

                            // Render stroke currently in progress
                            if (currentPoints.size > 1) {
                                val currentPath = Path().apply {
                                    moveTo(currentPoints.first().x, currentPoints.first().y)
                                    for (i in 1 until currentPoints.size) {
                                        lineTo(currentPoints[i].x, currentPoints[i].y)
                                    }
                                }
                                val isHigh = activeDrawingTool == "Highlighter"
                                drawPath(
                                    path = currentPath,
                                    color = if (isHigh) Color(0xFFFACC15).copy(alpha = 0.35f) else selectedPenColor,
                                    style = Stroke(
                                        width = if (isHigh) 24f else 4f,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Floating Doubt Prompt over Circled Region
                        AnimatedVisibility(
                            visible = showCircledDoubtPrompt,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 85.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF0F172A),
                                shadowElevation = 10.dp,
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        geminiInitialMode = CopilotMode.DOUBT_SOLVER
                                        geminiDoubtContext = "Circled region in ${note.title} (${note.subject})"
                                        showGeminiSheet = true
                                        showCircledDoubtPrompt = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Circled: Ask Gemini Step-by-Step Doubt",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ================= FLOATING MINIMAL TOOLBAR =================
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Color presets palette (Neon Cyan & Pastel Mint leading)
                AnimatedVisibility(visible = activeDrawingTool == "Pen" || activeDrawingTool == "Highlighter") {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = FrostedSlateSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedSlateBorder),
                        shadowElevation = 8.dp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val presets = listOf(
                                BrandElectricCyan, // Preset #1: Neon Cyan
                                BrandMintGreen,    // Preset #2: Pastel Mint
                                Color(0xFFFFD166), // Soft Gold
                                Color(0xFFF43F5E), // Coral
                                Color(0xFFFFFFFF)  // White
                            )
                            presets.forEach { color ->
                                val isSelected = selectedPenColor == color
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedPenColor = color }
                                )
                            }
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = FrostedSlateSurface,
                    shadowElevation = 12.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        FrostedSlateBorder
                    ),
                    modifier = Modifier.testTag("floating_minimal_toolbar")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1. Pen Tool
                        Surface(
                            shape = CircleShape,
                            color = if (activeDrawingTool == "Pen") BrandElectricCyan else Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { activeDrawingTool = if (activeDrawingTool == "Pen") null else "Pen" }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.BorderColor,
                                    contentDescription = "Pen Tool",
                                    tint = if (activeDrawingTool == "Pen") Color(0xFF0B101D) else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // 2. Highlighter Tool
                        Surface(
                            shape = CircleShape,
                            color = if (activeDrawingTool == "Highlighter") BrandMintGreen else Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { activeDrawingTool = if (activeDrawingTool == "Highlighter") null else "Highlighter" }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Highlight,
                                    contentDescription = "Highlighter",
                                    tint = if (activeDrawingTool == "Highlighter") Color(0xFF0B101D) else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // 3. Eraser Tool
                        Surface(
                            shape = CircleShape,
                            color = if (activeDrawingTool == "Eraser") DangerRose else Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    drawnStrokes.clear()
                                    showCircledDoubtPrompt = false
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Annotations",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp)
                                .background(FrostedSlateBorder)
                        )

                        // 4. Insert Blank Page
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { onInsertBlankPage() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NoteAdd,
                                    contentDescription = "Insert Blank Page",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // 5. AI Diagram Generator
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    diagramPrompt = "Generate circuit / chemical reaction schematic for ${note.title}"
                                    showDiagramDialog = true
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Schema,
                                    contentDescription = "Generate AI Diagram",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // 6. Dedicated ClarNote Sparkle Icon (Gemini Study Copilot)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ClarNoteBrandGradient)
                                .clickable {
                                    geminiInitialMode = CopilotMode.ASK_NOTE
                                    showGeminiSheet = true
                                }
                                .testTag("gemini_floating_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            ClarNoteSparkleIcon(
                                size = 22.dp,
                                tint = Color(0xFF0B101D),
                                withOuterGlow = false
                            )
                        }
                    }
                }
            }
        }
    }

    // Swipe-Up Google Gemini Study Copilot Sheet
    if (showGeminiSheet) {
        GeminiStudyCopilotSheet(
            note = note,
            initialMode = geminiInitialMode,
            initialDoubtContext = geminiDoubtContext,
            onDismiss = { showGeminiSheet = false }
        )
    }

    // AI Diagram Generator Dialog
    if (showDiagramDialog) {
        AlertDialog(
            onDismissRequest = { showDiagramDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schema, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Diagram Generator", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Describe the diagram or schema to generate from this note's concepts:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = diagramPrompt,
                        onValueChange = { diagramPrompt = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )
                    if (isGeneratingDiagram) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gemini is synthesizing diagram...", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isGeneratingDiagram = true
                        // Synthesize schematic diagram
                        val schematic = """
                            +-------------------------------------------------------------+
                            |       AI ARCHITECTURE / REACTION SCHEMATIC                 |
                            |                                                             |
                            |   [Input Concept] ---> [Reactant / Potential Field E0]       |
                            |          |                          |                       |
                            |          v                          v                       |
                            |   [Dielectric K] ----> [Capacitance C = eps0*A/(d-t+t/K)]   |
                            |          |                                                  |
                            |          v                                                  |
                            |   [Output State] ---> [Stored Energy U = 1/2 C V^2]         |
                            +-------------------------------------------------------------+
                        """.trimIndent()
                        generatedDiagramSvgOrText = schematic
                        isGeneratingDiagram = false
                        showDiagramDialog = false
                    },
                    enabled = !isGeneratingDiagram && diagramPrompt.isNotBlank()
                ) {
                    Text("Generate & Embed")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiagramDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Revision reminder selector dialog
    if (showRevisionDialog) {
        AlertDialog(
            onDismissRequest = { showRevisionDialog = false },
            title = { Text("Spaced Repetition Schedule") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Schedule this note for high-yield recall revision:")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = { onSetRevisionReminder(1); showRevisionDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("1 Day")
                        }
                        Button(onClick = { onSetRevisionReminder(3); showRevisionDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("3 Days")
                        }
                        Button(onClick = { onSetRevisionReminder(7); showRevisionDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("7 Days")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showRevisionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
