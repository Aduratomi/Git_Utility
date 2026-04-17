package com.example.gitutility.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * NoteDao (Data Access Object) defines the "queries" we can run on the notes table.
 * It's where we tell the app how to read, save, and delete notes.
 */
@Dao
interface NoteDao {
    /**
     * Gets all notes from the database. 
     * Returns a Flow, which is like a pipe that automatically sends new data 
     * whenever the notes in the database change.
     */
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Finds one specific note by its ID number.
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    /**
     * Adds a new note to the database. 
     * REPLACE means if the note already exists, just overwrite it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    /**
     * Updates an existing note row with new information.
     */
    @Update
    suspend fun updateNote(note: Note)

    /**
     * Removes a note from the database entirely.
     */
    @Delete
    suspend fun deleteNote(note: Note)
}
