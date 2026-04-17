package com.example.gitutility.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Note is a data class that represents a "notes" table in our database.
 * Each object created from this class will be a single row in that table.
 */
@Entity(tableName = "notes")
data class Note(
    // The @PrimaryKey is a unique ID for each note. autoGenerate = true makes Room handle IDs for us.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,    // The headline of the note
    val content: String,  // The body text of the note
    
    // Stores the exact time the note was saved (as a number)
    val timestamp: Long = System.currentTimeMillis(),
    
    val color: Int = 0    // Can be used to set a specific color for the note's card
)
