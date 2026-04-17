package com.example.gitutility.data.repository

import com.example.gitutility.data.local.Task
import com.example.gitutility.data.local.TaskDao
import kotlinx.coroutines.flow.Flow

/**
 * TaskRepository handles the data operations for the Task feature.
 */
class TaskRepository(private val taskDao: TaskDao) {
    // Provides a stream of all tasks directly from the DAO
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}
