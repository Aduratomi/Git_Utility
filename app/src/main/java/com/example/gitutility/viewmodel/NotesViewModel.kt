package com.example.gitutility.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitutility.data.local.Note
import com.example.gitutility.data.local.NoteDatabase
import com.example.gitutility.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * NotesViewModel manages the state and logic for the Notes screen.
 * It uses 'AndroidViewModel' because it needs the Application context for the database.
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {
    // DAO and Repository for managing local Note storage
    private val noteDao = NoteDatabase.getInstance(application).noteDao
    private val repository = NoteRepository(noteDao)

    /**
     * 'notes' is a live stream of all notes currently in the database.
     * It uses 'stateIn' to make the data available as a StateFlow for the UI.
     */
    val notes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keeps flow alive during orientation changes
        initialValue = emptyList()
    )

    // Tracks if the "Add Note" dialog is visible
    private val _isAddingNote = MutableStateFlow(false)
    val isAddingNote = _isAddingNote.asStateFlow()

    // Holds the note currently being edited, or null if not editing
    private val _editingNote = MutableStateFlow<Note?>(null)
    val editingNote = _editingNote.asStateFlow()

    /**
     * Shows the "Add Note" dialog.
     */
    fun onAddNoteClick() {
        _isAddingNote.value = true
    }

    /**
     * Closes any open Add or Edit dialogs.
     */
    fun onDismissDialog() {
        _isAddingNote.value = false
        _editingNote.value = null
    }

    /**
     * Called when a user taps a note, setting it as the note to be edited.
     */
    fun onNoteClick(note: Note) {
        _editingNote.value = note
    }

    /**
     * Saves a new note to the database.
     */
    fun saveNote(title: String, content: String, color: Int = 0) {
        if (title.isBlank() && content.isBlank()) return
        
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                color = color
            )
            repository.insertNote(newNote)
            _isAddingNote.value = false // Close dialog after saving
        }
    }

    /**
     * Updates an existing note with new title or content.
     */
    fun updateNote(note: Note, title: String, content: String, color: Int = 0) {
        if (title.isBlank() && content.isBlank()) return
        
        viewModelScope.launch {
            val updatedNote = note.copy(
                title = title,
                content = content,
                color = color,
                timestamp = System.currentTimeMillis() // Update the timestamp
            )
            repository.updateNote(updatedNote)
            _editingNote.value = null // Close edit dialog after update
        }
    }

    /**
     * Permanently deletes a note from the database.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
