package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.NoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isTrash = 0 AND isLockedVault = 0 ORDER BY createdAt DESC")
    fun getActiveNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM notes WHERE isTrash = 0 AND isLockedVault = 0 AND isStarred = 1 ORDER BY createdAt DESC")
    fun getStarredNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM notes WHERE isTrash = 0 AND isLockedVault = 0 AND isMistakeDiary = 1 ORDER BY createdAt DESC")
    fun getMistakeDiaryNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM notes WHERE isTrash = 0 AND isLockedVault = 1 ORDER BY createdAt DESC")
    fun getVaultNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM notes WHERE isTrash = 1 ORDER BY deletedTimestamp DESC")
    fun getTrashNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteItem?>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteByIdSync(id: Long): NoteItem?

    @Query("SELECT * FROM notes WHERE sha256Hash = :hash AND isTrash = 0")
    suspend fun findBySha256(hash: String): List<NoteItem>

    @Query("SELECT * FROM notes WHERE isTrash = 0")
    suspend fun getAllActiveList(): List<NoteItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteItem): Long

    @Update
    suspend fun updateNote(note: NoteItem)

    @Query("UPDATE notes SET isTrash = 1, deletedTimestamp = :timestamp WHERE id = :id")
    suspend fun moveToTrash(id: Long, timestamp: Long)

    @Query("UPDATE notes SET isTrash = 0, deletedTimestamp = NULL WHERE id = :id")
    suspend fun restoreFromTrash(id: Long)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deletePermanently(id: Long)

    @Query("DELETE FROM notes WHERE isTrash = 1")
    suspend fun emptyTrash()
}
