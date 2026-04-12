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

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDatabase.getInstance(application).noteDao
    private val repository = NoteRepository(noteDao)

    val notes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isAddingNote = MutableStateFlow(false)
    val isAddingNote = _isAddingNote.asStateFlow()

    fun onAddNoteClick() {
        _isAddingNote.value = true
    }

    fun onDismissDialog() {
        _isAddingNote.value = false
    }

    fun saveNote(title: String, content: String, color: Int = 0) {
        if (title.isBlank() && content.isBlank()) return
        
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                color = color
            )
            repository.insertNote(newNote)
            _isAddingNote.value = false
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
