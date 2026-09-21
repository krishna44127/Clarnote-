package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.GeminiStudyCopilot
import com.example.data.ai.QuizQuestion
import com.example.data.model.NoteItem
import com.example.ui.components.ClarNoteSparkleIcon
import com.example.ui.theme.BrandAquaBlue
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.BrandMintGreen
import com.example.ui.theme.ClarNoteBrandGradient
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald
import kotlinx.coroutines.launch

enum class CopilotMode(val title: String, val icon: ImageVector) {
    ASK_NOTE("Ask Note", Icons.Default.QuestionAnswer),
    FORMULAS("1-Tap Formulas", Icons.Default.Functions),
    DOUBT_SOLVER("Doubt Solver", Icons.Default.Psychology),
    INSTANT_QUIZ("Instant Quiz", Icons.Default.FlashOn),
    VOICE_SUMMARY("Voice Summary", Icons.Default.Mic)
}

data class ChatMessage(
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiStudyCopilotSheet(
    note: NoteItem,
    onDismiss: () -> Unit,
    initialMode: CopilotMode = CopilotMode.ASK_NOTE,
    initialDoubtContext: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedMode by remember { mutableStateOf(initialMode) }
    var activeAiModel by remember { mutableStateOf("Flash 2.5") } // "Flash 2.5", "Pro 2.5", "Imagen 3"

    // Mode 1: Chat with Note
    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage(
                isUser = false,
                text = "Hello! I am your Gemini Study Copilot. Ask me anything about '${note.title}', or tap one of the suggested study prompts below!"
            )
        )
    }
    var currentQuestion by remember { mutableStateOf("") }
    var isChatLoading by remember { mutableStateOf(false) }

    // Mode 2: Formula & Reaction Extractor
    var extractedFormulasText by remember { mutableStateOf("") }
    var isFormulasLoading by remember { mutableStateOf(false) }

    // Mode 3: Doubt Solver (Circle to Ask)
    var doubtInput by remember { mutableStateOf(if (initialDoubtContext.isNotBlank()) "Please explain the circled portion of my note step-by-step." else "") }
    var doubtSolutionText by remember { mutableStateOf("") }
    var isDoubtLoading by remember { mutableStateOf(false) }

    // Mode 4: Instant Quiz (5 MCQs)
    val quizList = remember { mutableStateListOf<QuizQuestion>() }
    var isQuizLoading by remember { mutableStateOf(false) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionId -> selectedOptionIndex

    // Mode 5: Voice Lecture Summary
    var voiceSummaryText by remember { mutableStateOf("") }
    var isVoiceSummaryLoading by remember { mutableStateOf(false) }

    // Auto trigger initial actions based on mode
    LaunchedEffect(selectedMode) {
        when (selectedMode) {
            CopilotMode.FORMULAS -> {
                if (extractedFormulasText.isBlank() && !isFormulasLoading) {
                    isFormulasLoading = true
                    val result = GeminiStudyCopilot.extractFormulasAndReactions(note)
                    extractedFormulasText = result
                    isFormulasLoading = false
                }
            }
            CopilotMode.INSTANT_QUIZ -> {
                if (quizList.isEmpty() && !isQuizLoading) {
                    isQuizLoading = true
                    val questions = GeminiStudyCopilot.generateQuiz(note)
                    quizList.clear()
                    quizList.addAll(questions)
                    isQuizLoading = false
                }
            }
            CopilotMode.VOICE_SUMMARY -> {
                if (voiceSummaryText.isBlank() && !isVoiceSummaryLoading) {
                    isVoiceSummaryLoading = true
                    val res = GeminiStudyCopilot.summarizeVoiceLecture(note)
                    voiceSummaryText = res
                    isVoiceSummaryLoading = false
                }
            }
            else -> {}
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .testTag("gemini_study_copilot_sheet"),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Gemini Study Copilot",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "${note.subject} • ${note.title.take(28)}...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            // In-Chat Model Switcher (Flash / Pro / Image Gen)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Model:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                listOf(
                    Triple("Flash 2.5", "Fast & Agile", Icons.Default.Bolt),
                    Triple("Pro 2.5", "Deep Reasoning", Icons.Default.Psychology),
                    Triple("Image Gen", "Visual Diagrams", Icons.Default.Image)
                ).forEach { (modelName, desc, icon) ->
                    val isSelected = activeAiModel == modelName
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) PrimaryBlue.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { activeAiModel = modelName }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = modelName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Mode Selector Tabs (Scrollable)
            ScrollableTabRow(
                selectedTabIndex = selectedMode.ordinal,
                edgePadding = 16.dp,
                divider = {}
            ) {
                CopilotMode.values().forEach { mode ->
                    Tab(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = mode },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = mode.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(mode.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body content according to selectedMode
            when (selectedMode) {
                CopilotMode.ASK_NOTE -> {
                    AskThisNoteView(
                        note = note,
                        messages = chatMessages,
                        currentQuestion = currentQuestion,
                        isLoading = isChatLoading,
                        onQuestionChange = { currentQuestion = it },
                        onSendMessage = { q ->
                            if (q.isNotBlank()) {
                                chatMessages.add(ChatMessage(isUser = true, text = q))
                                currentQuestion = ""
                                isChatLoading = true
                                scope.launch {
                                    val answer = GeminiStudyCopilot.askThisNote(note, q)
                                    chatMessages.add(ChatMessage(isUser = false, text = answer))
                                    isChatLoading = false
                                }
                            }
                        }
                    )
                }
                CopilotMode.FORMULAS -> {
                    FormulasExtractorView(
                        formulasText = extractedFormulasText,
                        isLoading = isFormulasLoading,
                        onRegenerate = {
                            scope.launch {
                                isFormulasLoading = true
                                extractedFormulasText = GeminiStudyCopilot.extractFormulasAndReactions(note)
                                isFormulasLoading = false
                            }
                        },
                        onCopy = { text ->
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("ClarNote Formulas", text))
                            Toast.makeText(context, "Formulas copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                CopilotMode.DOUBT_SOLVER -> {
                    DoubtSolverView(
                        note = note,
                        doubtInput = doubtInput,
                        initialContext = initialDoubtContext,
                        solutionText = doubtSolutionText,
                        isLoading = isDoubtLoading,
                        onDoubtInputChange = { doubtInput = it },
                        onSolveDoubt = { query ->
                            scope.launch {
                                isDoubtLoading = true
                                doubtSolutionText = GeminiStudyCopilot.solveCircledDoubt(note, query, initialDoubtContext)
                                isDoubtLoading = false
                            }
                        }
                    )
                }
                CopilotMode.INSTANT_QUIZ -> {
                    InstantQuizView(
                        quizQuestions = quizList,
                        isLoading = isQuizLoading,
                        userAnswers = userAnswers,
                        onSelectAnswer = { qId, optIndex ->
                            userAnswers[qId] = optIndex
                        },
                        onRegenerate = {
                            scope.launch {
                                isQuizLoading = true
                                userAnswers.clear()
                                quizList.clear()
                                quizList.addAll(GeminiStudyCopilot.generateQuiz(note))
                                isQuizLoading = false
                            }
                        }
                    )
                }
                CopilotMode.VOICE_SUMMARY -> {
                    VoiceSummaryView(
                        summaryText = voiceSummaryText,
                        isLoading = isVoiceSummaryLoading,
                        onRegenerate = {
                            scope.launch {
                                isVoiceSummaryLoading = true
                                voiceSummaryText = GeminiStudyCopilot.summarizeVoiceLecture(note)
                                isVoiceSummaryLoading = false
                            }
                        },
                        onCopy = { text ->
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("ClarNote Lecture Summary", text))
                            Toast.makeText(context, "Summary copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// 1. "Ask This Note" View
@Composable
private fun AskThisNoteView(
    note: NoteItem,
    messages: List<ChatMessage>,
    currentQuestion: String,
    isLoading: Boolean,
    onQuestionChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    val quickQuestions = listOf(
        "Explain King's property simply",
        "Key derivations & formulas",
        "Common exam pitfalls & traps",
        "Summarize this in 3 bullet points"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick prompts suggestions
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSendMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.isUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (isUser) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 4.dp
                            ),
                            color = PrimaryBlue,
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 16.dp
                            ),
                            color = FrostedSlateSurface,
                            border = BorderStroke(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(
                                        BrandElectricCyan.copy(alpha = 0.55f),
                                        BrandAquaBlue.copy(alpha = 0.25f),
                                        BrandMintGreen.copy(alpha = 0.55f)
                                    )
                                )
                            ),
                            modifier = Modifier.widthIn(max = 320.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                ) {
                                    ClarNoteSparkleIcon(size = 14.dp, tint = BrandElectricCyan)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ClarNote Gemini Copilot",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandElectricCyan
                                    )
                                }
                                AiMessageLinesRenderer(text = msg.text)
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gemini is reading note...", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Bottom Input Row with Attachment Hub (+)
        var showAttachmentOptions by remember { mutableStateOf(false) }

        if (showAttachmentOptions) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.clickable {
                        showAttachmentOptions = false
                        onSendMessage("[Attached Vault File: Physics_Formula_CheatSheet.pdf] ")
                    }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Attach from Vault", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.clickable {
                        showAttachmentOptions = false
                        onSendMessage("[Attached Device Document: Recent_Class_Scan.png] ")
                    }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Attach from Device", fontSize = 11.sp)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "+" Attachment Hub Button
            IconButton(
                onClick = { showAttachmentOptions = !showAttachmentOptions },
                modifier = Modifier.size(38.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (showAttachmentOptions) PrimaryBlue else MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Attach Files",
                            tint = if (showAttachmentOptions) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            OutlinedTextField(
                value = currentQuestion,
                onValueChange = onQuestionChange,
                placeholder = { Text("Ask Gemini about this note...", fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("copilot_chat_input"),
                shape = RoundedCornerShape(20.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors()
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onSendMessage(currentQuestion) },
                enabled = currentQuestion.isNotBlank() && !isLoading,
                modifier = Modifier.testTag("copilot_send_button")
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (currentQuestion.isNotBlank()) PrimaryBlue else MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Question",
                            tint = if (currentQuestion.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// 2. Formulas & Reactions Extractor View
@Composable
private fun FormulasExtractorView(
    formulasText: String,
    isLoading: Boolean,
    onRegenerate: () -> Unit,
    onCopy: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1-Tap Formula & Reaction Sheet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                IconButton(onClick = { onCopy(formulasText) }, enabled = formulasText.isNotBlank()) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Formulas", tint = PrimaryBlue)
                }
                TextButton(onClick = onRegenerate, enabled = !isLoading) {
                    Text("Re-extract")
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp), color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Gemini scanning pages for formulas & reactions...")
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(
                            BrandElectricCyan.copy(alpha = 0.7f),
                            BrandAquaBlue.copy(alpha = 0.35f),
                            BrandMintGreen.copy(alpha = 0.7f)
                        )
                    )
                ),
                colors = CardDefaults.cardColors(containerColor = FrostedSlateSurface)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        AiMessageLinesRenderer(text = formulasText)
                    }
                }
            }
        }
    }
}

// 3. Doubt Solver View (Circle to Ask)
@Composable
private fun DoubtSolverView(
    note: NoteItem,
    doubtInput: String,
    initialContext: String,
    solutionText: String,
    isLoading: Boolean,
    onDoubtInputChange: (String) -> Unit,
    onSolveDoubt: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Doubt Solver (Circle to Ask)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Highlight or type the difficult step, diagram, or equation you need Gemini to unpack:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (initialContext.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PrimaryBlue.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    text = "🎯 Canvas Target: $initialContext",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 11.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = doubtInput,
                onValueChange = onDoubtInputChange,
                placeholder = { Text("E.g., How does King's rule simplify this integral?", fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                maxLines = 2
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onSolveDoubt(doubtInput) },
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Solve")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Solving doubt step-by-step...")
                }
            }
        } else if (solutionText.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(
                            BrandElectricCyan.copy(alpha = 0.7f),
                            BrandAquaBlue.copy(alpha = 0.35f),
                            BrandMintGreen.copy(alpha = 0.7f)
                        )
                    )
                ),
                colors = CardDefaults.cardColors(containerColor = FrostedSlateSurface)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        AiMessageLinesRenderer(text = solutionText)
                    }
                }
            }
        }
    }
}

// 4. Instant Quiz View (5 tough MCQs)
@Composable
private fun InstantQuizView(
    quizQuestions: List<QuizQuestion>,
    isLoading: Boolean,
    userAnswers: Map<Int, Int>,
    onSelectAnswer: (Int, Int) -> Unit,
    onRegenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Instant 5-MCQ Quiz & Flashcards",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            TextButton(onClick = onRegenerate, enabled = !isLoading) {
                Text("New Quiz")
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Gemini generating 5 exam questions from your notes...")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(quizQuestions) { question ->
                    val selectedOption = userAnswers[question.id]

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Q${question.id}. ${question.question}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            question.options.forEachIndexed { optIndex, optionText ->
                                val isSelected = selectedOption == optIndex
                                val isCorrect = question.correctIndex == optIndex
                                val showFeedback = selectedOption != null

                                val backgroundColor = when {
                                    showFeedback && isCorrect -> SuccessEmerald.copy(alpha = 0.2f)
                                    showFeedback && isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                    isSelected -> PrimaryBlue.copy(alpha = 0.12f)
                                    else -> MaterialTheme.colorScheme.surface
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = backgroundColor,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onSelectAnswer(question.id, optIndex) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + optIndex)}. ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            // Explanation feedback
                            if (selectedOption != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "💡 Explanation: ${question.explanation}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
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

// 5. Voice Lecture Summary View
@Composable
private fun VoiceSummaryView(
    summaryText: String,
    isLoading: Boolean,
    onRegenerate: () -> Unit,
    onCopy: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Voice Lecture Summarizer",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                IconButton(onClick = { onCopy(summaryText) }, enabled = summaryText.isNotBlank()) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Summary", tint = PrimaryBlue)
                }
                TextButton(onClick = onRegenerate, enabled = !isLoading) {
                    Text("Re-summarize")
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Gemini transcribing and extracting lecture takeaways...")
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(
                            BrandElectricCyan.copy(alpha = 0.7f),
                            BrandAquaBlue.copy(alpha = 0.35f),
                            BrandMintGreen.copy(alpha = 0.7f)
                        )
                    )
                ),
                colors = CardDefaults.cardColors(containerColor = FrostedSlateSurface)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        AiMessageLinesRenderer(text = summaryText)
                    }
                }
            }
        }
    }
}

@Composable
fun AiMessageLinesRenderer(
    text: String,
    modifier: Modifier = Modifier
) {
    val lines = remember(text) { text.lines() }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { rawLine ->
            val trimmed = rawLine.trim()
            if (trimmed.isEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                return@forEach
            }

            val isBullet = trimmed.startsWith("* ") ||
                    trimmed.startsWith("- ") ||
                    trimmed.startsWith("• ") ||
                    (trimmed.length > 2 && trimmed[0].isDigit() && (trimmed[1] == '.' || trimmed[1] == ')'))
            val isFormula = trimmed.contains("=") &&
                    (trimmed.contains("+") || trimmed.contains("-") || trimmed.contains("\\") || trimmed.contains("^") || trimmed.contains("/") || trimmed.contains("∫") || trimmed.contains("∑"))

            if (isBullet || isFormula) {
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    ClarNoteSparkleIcon(
                        size = 13.dp,
                        tint = BrandElectricCyan,
                        withOuterGlow = true,
                        modifier = Modifier.padding(top = 3.dp, end = 8.dp)
                    )
                    val contentLine = when {
                        trimmed.startsWith("* ") || trimmed.startsWith("- ") -> trimmed.removeRange(0, 2)
                        trimmed.startsWith("• ") -> trimmed.removePrefix("• ")
                        else -> trimmed
                    }
                    Text(
                        text = contentLine,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFF1F5F9),
                            lineHeight = 21.sp,
                            fontWeight = if (isFormula) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            } else {
                Text(
                    text = rawLine,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFF1F5F9),
                        lineHeight = 21.sp
                    )
                )
            }
        }
    }
}

