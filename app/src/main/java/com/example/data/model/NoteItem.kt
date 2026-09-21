package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NoteType {
    PDF, IMAGE, VOICE, TEXT, VIDEO
}

enum class UploadStatus {
    UPLOADING, SYNCED, OFFLINE_QUEUED
}

data class VideoBookmark(
    val timestampSeconds: Int,
    val formattedTime: String,
    val label: String
)

data class Flashcard(
    val question: String,
    val answer: String
)

data class FormulaItem(
    val title: String,
    val formula: String,
    val explanation: String
)

@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String = "",
    val ocrText: String = "",
    val type: NoteType = NoteType.TEXT,
    val subject: String = "Physics",
    val folder: String = "Physics",
    val tags: String = "", // e.g. "#Optics, #Formulas, #PyQ"
    val fileUri: String? = null,
    val originalSizeBytes: Long = 1024 * 600,
    val compressedSizeBytes: Long = 1024 * 180,
    val sha256Hash: String = "",
    val isDuplicate: Boolean = false,
    val duplicateOfTitle: String? = null,
    val isNearDuplicate: Boolean = false,
    val nearDuplicateReason: String? = null,
    val isStarred: Boolean = false,
    val isMistakeDiary: Boolean = false,
    val isLockedVault: Boolean = false,
    val isTrash: Boolean = false,
    val deletedTimestamp: Long? = null,
    val uploadStatus: UploadStatus = UploadStatus.SYNCED,
    val uploadProgress: Float = 1.0f,
    val revisionDueDate: Long? = null,
    val revisionIntervalDays: Int = 0,
    val videoBookmarksJson: String = "[]",
    val flashcardsJson: String = "[]",
    val extractedFormulasJson: String = "[]",
    val drawingStrokesJson: String = "[]",
    val pagesCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
