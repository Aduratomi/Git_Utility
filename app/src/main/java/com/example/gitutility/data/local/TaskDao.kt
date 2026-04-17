package com.example.gitutility.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * TaskDao defines how we interact with the "tasks" table in the local database.
 */
@Dao
interface TaskDao {
    /**
     * Watches the tasks table and gives us a list of tasks whenever anything changes.
     */
    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Saves a task to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /**
     * Edits an existing task in the database.
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task from the database.
     */
    @Delete
    suspend fun deleteTask(task: Task)
}
