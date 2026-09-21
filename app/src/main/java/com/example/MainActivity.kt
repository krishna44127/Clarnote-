package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.ui.ClarNoteEvent
import com.example.ui.ClarNoteViewModel
import com.example.ui.components.CreateFolderDialog
import com.example.ui.components.FloatingQuickActionButton
import com.example.ui.components.GeminiStudyCopilotSheet
import com.example.ui.components.InAppScannerDialog
import com.example.ui.components.MultiPagePdfMergeDialog
import com.example.ui.components.NoteDetailScreen
import com.example.ui.components.QuickActionMenuSheet
import com.example.ui.components.ScratchpadDialog
import com.example.ui.components.SettingsSheet
import com.example.ui.components.VaultDialog
import com.example.ui.components.VoiceRecorderDialog
import com.example.ui.screens.FolderDetailScreen
import com.example.ui.components.ClarNoteSparkleIcon
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PermissionPrimerScreen
import com.example.ui.screens.RecentStarredScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.BrandElectricCyan
import com.example.ui.theme.ClarNoteBrandGradient
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.FrostedSlateBorder
import com.example.ui.theme.FrostedSlateSurface
import com.example.ui.theme.MidnightNavyBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SuccessEmerald

class MainActivity : ComponentActivity() {
    private val viewModel: ClarNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by viewModel.appTheme.collectAsState()
            val accentColor by viewModel.accentColor.collectAsState()
            MyApplicationTheme(appTheme = appTheme, accentColor = accentColor) {
                ClarNoteMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClarNoteMainApp(viewModel: ClarNoteViewModel) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("clarnote_prefs", android.content.Context.MODE_PRIVATE) }
    var hasSeenPrimer by remember { mutableStateOf(prefs.getBoolean("has_seen_permission_primer", false)) }
    val snackbarHostState = remember { SnackbarHostState() }

    // State collections
    val activeNotes by viewModel.activeNotes.collectAsState()
    val starredNotes by viewModel.starredNotes.collectAsState()
    val mistakeDiaryNotes by viewModel.mistakeDiaryNotes.collectAsState()
    val vaultNotes by viewModel.vaultNotes.collectAsState()
    val trashNotes by viewModel.trashNotes.collectAsState()
    val customFolders by viewModel.customFolders.collectAsState()

    val storageLocation by viewModel.storageLocation.collectAsState()
    val googleAccountEmail by viewModel.googleAccountEmail.collectAsState()
    val syncOnlyWifi by viewModel.syncOnlyWifi.collectAsState()
    val autoSyncFrequency by viewModel.autoSyncFrequency.collectAsState()
    val geminiDefaultModel by viewModel.geminiDefaultModel.collectAsState()
    val geminiResponseTone by viewModel.geminiResponseTone.collectAsState()
    val appTheme by viewModel.appTheme.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val customWallpaperUri by viewModel.customWallpaperUri.collectAsState()
    val wallpaperBlur by viewModel.wallpaperBlur.collectAsState()
    val wallpaperDarkness by viewModel.wallpaperDarkness.collectAsState()
    val biometricLockEnabled by viewModel.biometricLockEnabled.collectAsState()
    val trashRetentionDays by viewModel.trashRetentionDays.collectAsState()

    val selectedTab by viewModel.selectedTab.collectAsState()
    val activeFolder by viewModel.activeFolder.collectAsState()
    val selectedNoteIds by viewModel.selectedNoteIds.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedTag by viewModel.selectedTag.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val recentSubFilter by viewModel.recentSubFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val autoCategorizeEnabled by viewModel.autoCategorizeEnabled.collectAsState()
    val silentSyncEnabled by viewModel.silentSyncEnabled.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val isVaultUnlocked by viewModel.isVaultUnlocked.collectAsState()
    val activeDetailNote by viewModel.activeDetailNote.collectAsState()

    // Real Native SAF System File Picker Launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (!uris.isNullOrEmpty()) {
            viewModel.importFilesFromUris(uris, activeFolder)
        }
    }

    // First-Launch Permission Primer Screen (Shown ONCE only)
    if (!hasSeenPrimer) {
        PermissionPrimerScreen(
            onContinue = {
                prefs.edit().putBoolean("has_seen_permission_primer", true).apply()
                hasSeenPrimer = true
            }
        )
        return
    }

    // Dialog & Sheet states
    var showQuickActions by remember { mutableStateOf(false) }
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showScannerDialog by remember { mutableStateOf(false) }
    var showPdfMergerDialog by remember { mutableStateOf(false) }
    var showVoiceMemoDialog by remember { mutableStateOf(false) }
    var showScratchpadDialog by remember { mutableStateOf(false) }
    var showVaultDialog by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showGeminiFolderSheet by remember { mutableStateOf(false) }

    // Handle Toast / Undo Events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ClarNoteEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ClarNoteEvent.ShowUndoDelete -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Removed '${event.noteTitle}' to 30-day trash",
                        actionLabel = "UNDO",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoDelete()
                    }
                }
            }
        }
    }

    // 1. Note Detail Screen Overlay (Content First Canvas + Floating Minimal Toolbar)
    if (activeDetailNote != null) {
        NoteDetailScreen(
            note = activeDetailNote!!,
            onBack = { viewModel.closeNoteDetail() },
            onToggleStar = { viewModel.toggleStar(activeDetailNote!!) },
            onToggleMistakeDiary = { viewModel.toggleMistakeDiary(activeDetailNote!!) },
            onToggleVault = { viewModel.toggleVaultNote(activeDetailNote!!) },
            onSetRevisionReminder = { days -> viewModel.setRevisionReminder(activeDetailNote!!, days) },
            onInsertBlankPage = { viewModel.insertBlankPage(activeDetailNote!!) },
            onSaveDrawing = { strokes -> viewModel.saveNoteDrawing(activeDetailNote!!, strokes) },
            onAddVideoBookmark = { sec, time, note -> viewModel.addVideoBookmark(activeDetailNote!!, sec, time, note) },
            onDeleteNote = { viewModel.deleteNoteWithUndo(activeDetailNote!!.id, activeDetailNote!!.title) },
            onUpdateNoteContent = { updatedContent ->
                viewModel.updateNoteContent(activeDetailNote!!, activeDetailNote!!.title, updatedContent)
            }
        )
        return
    }

    // 2. Folder Inside Screen (On-Demand Loading for specific folder)
    if (activeFolder != null) {
        FolderDetailScreen(
            folderName = activeFolder!!,
            notes = activeNotes,
            availableFolders = customFolders.map { it.name },
            selectedTag = selectedTag,
            selectedType = selectedType,
            selectedNoteIds = selectedNoteIds,
            onSelectTag = { viewModel.selectTag(it) },
            onSelectType = { viewModel.selectType(it) },
            onBack = { viewModel.closeFolder() },
            onNoteClick = { viewModel.openNoteDetail(it) },
            onToggleStar = { viewModel.toggleStar(it) },
            onDeleteDuplicate = { viewModel.deleteNoteWithUndo(it.id, it.title) },
            onToggleSelection = { viewModel.toggleNoteSelection(it) },
            onSelectAll = { viewModel.selectAllInFolder(it) },
            onClearSelection = { viewModel.clearNoteSelection() },
            onPinSelected = { viewModel.pinSelectedNotes() },
            onMoveSelected = { viewModel.moveSelectedNotes(it) },
            onDeleteSelected = { viewModel.deleteSelectedNotesWithUndo() },
            onBatchUpload = { count -> viewModel.batchUploadNotes(activeFolder!!, count) },
            onOpenGeminiCopilot = { showGeminiFolderSheet = true },
            onPickSystemFiles = { filePickerLauncher.launch(arrayOf("*/*")) }
        )

        // Gemini Study Copilot at Folder Level
        if (showGeminiFolderSheet) {
            val sampleFolderNote = activeNotes.firstOrNull { it.subject.equals(activeFolder, ignoreCase = true) }
                ?: NoteItem(
                    title = "$activeFolder Study Overview",
                    content = "Core concepts, derivations, theorems and exam formulas for $activeFolder.",
                    ocrText = "Essential formulas and key definitions for $activeFolder.",
                    type = NoteType.TEXT,
                    subject = activeFolder!!
                )
            GeminiStudyCopilotSheet(
                note = sampleFolderNote,
                onDismiss = { showGeminiFolderSheet = false }
            )
        }
        return
    }

    // 3. Top-Level Scaffold with Glassmorphic Floating Navigation Dock
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MidnightNavyBg,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    notes = activeNotes,
                    subjects = customFolders,
                    pinnedNotes = starredNotes,
                    isSyncing = isSyncing,
                    onFolderClick = { folderName -> viewModel.openFolder(folderName) },
                    onNoteClick = { note -> viewModel.openNoteDetail(note) },
                    onSearchClick = { viewModel.selectTab(2) },
                    onSettingsClick = { showSettingsSheet = true },
                    onFabClick = { showQuickActions = true },
                    onOpenGeminiStudio = { showGeminiFolderSheet = true }
                )
                1 -> RecentStarredScreen(
                    activeNotes = activeNotes,
                    starredNotes = starredNotes,
                    mistakeDiaryNotes = mistakeDiaryNotes,
                    subFilter = recentSubFilter,
                    onSelectSubFilter = { viewModel.setRecentSubFilter(it) },
                    onNoteClick = { viewModel.openNoteDetail(it) },
                    onToggleStar = { viewModel.toggleStar(it) },
                    onDeleteDuplicate = { viewModel.deleteNoteWithUndo(it.id, it.title) }
                )
                2 -> SearchScreen(
                    notes = activeNotes,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onNoteClick = { viewModel.openNoteDetail(it) },
                    onToggleStar = { viewModel.toggleStar(it) },
                    onDeleteDuplicate = { viewModel.deleteNoteWithUndo(it.id, it.title) }
                )
            }

            // Glassmorphic Floating Dock: #101726 with border #1E2B45
            ClarNoteFloatingDock(
                selectedTab = selectedTab,
                onSelectTab = { viewModel.selectTab(it) },
                onOpenGeminiStudio = { showGeminiFolderSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }

    // Quick Actions Sheet (+)
    if (showQuickActions) {
        QuickActionMenuSheet(
            onDismiss = { showQuickActions = false },
            onOpenCreateFolder = { showCreateFolderDialog = true },
            onOpenScanner = { showScannerDialog = true },
            onOpenPdfMerger = { showPdfMergerDialog = true },
            onOpenVoiceMemo = { showVoiceMemoDialog = true },
            onOpenPdfImport = {
                // Real Native SAF System File Picker (Supports all study documents, PDFs, etc.)
                filePickerLauncher.launch(arrayOf("*/*"))
            },
            onOpenScratchpad = { showScratchpadDialog = true }
        )
    }

    // New Subject Folder Dialog (Popup right from Home FAB)
    if (showCreateFolderDialog) {
        CreateFolderDialog(
            onDismiss = { showCreateFolderDialog = false },
            onCreateFolder = { name, colorHex ->
                viewModel.createFolder(name, colorHex)
            }
        )
    }

    // In-App Document Auto-Scanner Dialog
    if (showScannerDialog) {
        InAppScannerDialog(
            onDismiss = { showScannerDialog = false },
            onSaveScan = { title, content, ocr, subject ->
                viewModel.uploadOrSaveNote(
                    title = title,
                    rawContent = content,
                    ocrExtractedText = ocr,
                    type = NoteType.IMAGE,
                    targetSubject = subject,
                    simulatedBytes = 3_200_000,
                    pagesCount = 1
                )
            }
        )
    }

    // Multi-Page Image to PDF Merger Dialog
    if (showPdfMergerDialog) {
        MultiPagePdfMergeDialog(
            onDismiss = { showPdfMergerDialog = false },
            onMergePdf = { title, content, pagesCount ->
                viewModel.uploadOrSaveNote(
                    title = title,
                    rawContent = content,
                    ocrExtractedText = "Merged continuous PDF with $pagesCount chapter pages. Includes derivation and solved numerical problems.",
                    type = NoteType.PDF,
                    targetSubject = "Physics",
                    simulatedBytes = 6_500_000,
                    pagesCount = pagesCount
                )
            }
        )
    }

    // Voice-to-Text Memo Dialog
    if (showVoiceMemoDialog) {
        VoiceRecorderDialog(
            onDismiss = { showVoiceMemoDialog = false },
            onSaveVoiceMemo = { title, transcript, subject ->
                viewModel.uploadOrSaveNote(
                    title = title,
                    rawContent = transcript,
                    ocrExtractedText = transcript,
                    type = NoteType.VOICE,
                    targetSubject = subject,
                    simulatedBytes = 950_000,
                    pagesCount = 1
                )
            }
        )
    }

    // Quick Text Scratchpad Dialog
    if (showScratchpadDialog) {
        ScratchpadDialog(
            onDismiss = { showScratchpadDialog = false },
            onSaveNote = { title, content ->
                viewModel.uploadOrSaveNote(
                    title = title,
                    rawContent = content,
                    ocrExtractedText = content,
                    type = NoteType.TEXT,
                    simulatedBytes = 150_000,
                    pagesCount = 1
                )
            }
        )
    }

    // Private / Locked Vault Dialog
    if (showVaultDialog) {
        VaultDialog(
            isUnlocked = isVaultUnlocked,
            vaultNotes = vaultNotes,
            onUnlock = { pin -> viewModel.unlockVault(pin) },
            onLock = { viewModel.lockVault() },
            onOpenNote = { note -> viewModel.openNoteDetail(note) },
            onDismiss = { showVaultDialog = false }
        )
    }

    // Settings Sheet (5 Structured Modules: Storage, Cloud Sync, Gemini AI, Personalization, Security & Trash)
    if (showSettingsSheet) {
        SettingsSheet(
            storageLocation = storageLocation,
            onSetStorageLocation = { viewModel.setStorageLocation(it) },
            onClearCache = { viewModel.clearCache() },
            googleAccountEmail = googleAccountEmail,
            onSwitchGoogleAccount = { viewModel.setGoogleAccountEmail(it) },
            syncOnlyWifi = syncOnlyWifi,
            onToggleSyncOnlyWifi = { viewModel.setSyncOnlyWifi(it) },
            autoSyncFrequency = autoSyncFrequency,
            onSetAutoSyncFrequency = { viewModel.setAutoSyncFrequency(it) },
            onTriggerSync = { viewModel.triggerManualSync() },
            isSyncing = isSyncing,
            geminiDefaultModel = geminiDefaultModel,
            onSetGeminiModel = { viewModel.setGeminiModel(it) },
            geminiResponseTone = geminiResponseTone,
            onSetResponseTone = { viewModel.setGeminiResponseTone(it) },
            appTheme = appTheme,
            onSetAppTheme = { viewModel.setAppTheme(it) },
            accentColor = accentColor,
            onSetAccentColor = { viewModel.setAccentColor(it) },
            customWallpaperUri = customWallpaperUri,
            onSetCustomWallpaperUri = { viewModel.setCustomWallpaperUri(it) },
            wallpaperBlur = wallpaperBlur,
            onSetWallpaperBlur = { viewModel.setWallpaperBlur(it) },
            wallpaperDarkness = wallpaperDarkness,
            onSetWallpaperDarkness = { viewModel.setWallpaperDarkness(it) },
            biometricLockEnabled = biometricLockEnabled,
            onToggleBiometricLock = { viewModel.setBiometricLock(it) },
            trashRetentionDays = trashRetentionDays,
            onSetTrashRetentionDays = { viewModel.setTrashRetentionDays(it) },
            trashNotes = trashNotes,
            onRestoreFromTrash = { viewModel.restoreFromTrash(it) },
            onEmptyTrash = { viewModel.emptyTrash() },
            onDismiss = { showSettingsSheet = false }
        )
    }
}

@Composable
fun ClarNoteFloatingDock(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    onOpenGeminiStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = FrostedSlateSurface.copy(alpha = 0.94f),
        border = BorderStroke(1.2.dp, FrostedSlateBorder),
        shadowElevation = 16.dp,
        modifier = modifier
            .padding(horizontal = 24.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tab 0: Home (Clean Folders Grid)
            DockNavigationItem(
                icon = if (selectedTab == 0) Icons.Filled.Folder else Icons.Outlined.Folder,
                label = "Home",
                isSelected = selectedTab == 0,
                onClick = { onSelectTab(0) },
                testTag = "nav_tab_home"
            )

            // Center Tab: Live Gemini Sparkle Star
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ClarNoteBrandGradient)
                    .clickable(onClick = onOpenGeminiStudio)
                    .testTag("nav_tab_gemini_sparkle"),
                contentAlignment = Alignment.Center
            ) {
                ClarNoteSparkleIcon(
                    size = 24.dp,
                    tint = Color(0xFF0B101D),
                    withOuterGlow = true
                )
            }

            // Tab 1: VIP Vault / Starred
            DockNavigationItem(
                icon = if (selectedTab == 1) Icons.Filled.Star else Icons.Outlined.StarBorder,
                label = "Vault",
                isSelected = selectedTab == 1,
                onClick = { onSelectTab(1) },
                testTag = "nav_tab_recent"
            )

            // Tab 2: Search
            DockNavigationItem(
                icon = if (selectedTab == 2) Icons.Filled.Search else Icons.Outlined.Search,
                label = "Search",
                isSelected = selectedTab == 2,
                onClick = { onSelectTab(2) },
                testTag = "nav_tab_search"
            )
        }
    }
}

@Composable
private fun DockNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val inactiveColor = Color(0xFF64748B) // Slate Gray
    val activeColor = BrandElectricCyan    // Glowing Electric Cyan

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}

