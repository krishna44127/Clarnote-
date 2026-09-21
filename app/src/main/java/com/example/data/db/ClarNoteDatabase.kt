package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.NoteItem
import com.example.data.model.NoteType
import com.example.data.model.UploadStatus

class Converters {
    @TypeConverter
    fun fromNoteType(value: NoteType): String = value.name

    @TypeConverter
    fun toNoteType(value: String): NoteType = try {
        NoteType.valueOf(value)
    } catch (e: Exception) {
        NoteType.TEXT
    }

    @TypeConverter
    fun fromUploadStatus(value: UploadStatus): String = value.name

    @TypeConverter
    fun toUploadStatus(value: String): UploadStatus = try {
        UploadStatus.valueOf(value)
    } catch (e: Exception) {
        UploadStatus.SYNCED
    }
}

@Database(entities = [NoteItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClarNoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
