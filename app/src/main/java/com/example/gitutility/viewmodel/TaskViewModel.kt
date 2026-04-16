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

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = NoteDatabase.getInstance(application).taskDao
    private val repository = TaskRepository(taskDao)

    val tasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isAddingTask = MutableStateFlow(false)
    val isAddingTask = _isAddingTask.asStateFlow()

    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask = _editingTask.asStateFlow()

    fun onAddTaskClick() {
        _isAddingTask.value = true
    }

    fun onDismissAddTask() {
        _isAddingTask.value = false
    }

    fun onEditTaskClick(task: Task) {
        _editingTask.value = task
    }

    fun onDismissEditTask() {
        _editingTask.value = null
    }

    fun saveTask(title: String, reminderTimestamp: Long? = null) {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            val newTask = Task(title = title, reminderTimestamp = reminderTimestamp)
            repository.insertTask(newTask)
            _isAddingTask.value = false
        }
    }

    fun updateTask(task: Task, newTitle: String, reminderTimestamp: Long? = null) {
        if (newTitle.isBlank()) return
        
        viewModelScope.launch {
            val updatedTask = task.copy(title = newTitle, reminderTimestamp = reminderTimestamp)
            repository.updateTask(updatedTask)
            _editingTask.value = null
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            repository.updateTask(updatedTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
