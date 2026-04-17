package com.example.gitutility.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Task represents a single to-do item in our local database.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,            // Description of what needs to be done
    val isCompleted: Boolean = false, // Whether the user has checked it off
    val timestamp: Long = System.currentTimeMillis(), // When it was created
    
    // An optional time for a reminder notification (null if no reminder set)
    val reminderTimestamp: Long? = null
)
