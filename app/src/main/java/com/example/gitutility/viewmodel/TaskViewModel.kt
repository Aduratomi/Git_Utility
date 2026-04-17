package com.example.gitutility.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitutility.data.local.NoteDatabase
import com.example.gitutility.data.local.Task
import com.example.gitutility.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * TaskViewModel manages all the logic for the Tasks screen.
 * It provides functions to add, update, delete, and toggle tasks.
 */
class TaskViewModel(application: Application) : AndroidViewModel(application) {
    // DAO and Repository for managing local Task storage
    private val taskDao = NoteDatabase.getInstance(application).taskDao
    private val repository = TaskRepository(taskDao)

    /**
     * 'tasks' is a real-time list of all tasks from the database.
     */
    val tasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Tracks if the "Add Task" dialog should be visible
    private val _isAddingTask = MutableStateFlow(false)
    val isAddingTask = _isAddingTask.asStateFlow()

    // Holds the task being edited, or null if not currently editing
    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask = _editingTask.asStateFlow()

    /**
     * Shows the add task dialog.
     */
    fun onAddTaskClick() {
        _isAddingTask.value = true
    }

    /**
     * Closes the add task dialog.
     */
    fun onDismissAddTask() {
        _isAddingTask.value = false
    }

    /**
     * Sets a specific task to be edited, showing the edit dialog.
     */
    fun onEditTaskClick(task: Task) {
        _editingTask.value = task
    }

    /**
     * Closes the edit task dialog.
     */
    fun onDismissEditTask() {
        _editingTask.value = null
    }

    /**
     * Saves a new task to the database.
     * @param title The description of the task.
     * @param reminderTimestamp The date/time for the reminder, if set.
     */
    fun saveTask(title: String, reminderTimestamp: Long? = null) {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            val newTask = Task(title = title, reminderTimestamp = reminderTimestamp)
            repository.insertTask(newTask)
            _isAddingTask.value = false // Automatically close dialog
        }
    }

    /**
     * Updates an existing task with a new title or reminder.
     */
    fun updateTask(task: Task, newTitle: String, reminderTimestamp: Long? = null) {
        if (newTitle.isBlank()) return
        
        viewModelScope.launch {
            val updatedTask = task.copy(title = newTitle, reminderTimestamp = reminderTimestamp)
            repository.updateTask(updatedTask)
            _editingTask.value = null // Automatically close dialog
        }
    }

    /**
     * Toggles the task's completion status (Checked/Unchecked).
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            repository.updateTask(updatedTask)
        }
    }

    /**
     * Permanently deletes a task.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
