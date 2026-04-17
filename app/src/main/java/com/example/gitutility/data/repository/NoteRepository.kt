package com.example.gitutility.data.repository

import com.example.gitutility.data.local.Note
import com.example.gitutility.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

/**
 * NoteRepository acts as a middle-man between the data (database) and the logic (ViewModel).
 * It abstracts the source of the data.
 */
class NoteRepository(private val noteDao: NoteDao) {
    // Provides a stream of all notes directly from the DAO
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}
