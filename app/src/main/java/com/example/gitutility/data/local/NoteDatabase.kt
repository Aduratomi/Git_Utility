package com.example.gitutility.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * NoteDatabase is the actual SQLite database on the phone.
 * It stores our "notes" and "tasks" tables.
 */
@Database(entities = [Note::class, Task::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {
    // Provides access to the DAOs
    abstract val noteDao: NoteDao
    abstract val taskDao: TaskDao

    companion object {
        const val DATABASE_NAME = "notes_db"

        // The single instance of the database used throughout the app
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        /**
         * Gets the existing database instance or creates a new one if it doesn't exist.
         */
        fun getInstance(context: Context): NoteDatabase {
            // synchronized prevents two threads from creating the database at the same time
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Wipes data if we change the version without a migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
