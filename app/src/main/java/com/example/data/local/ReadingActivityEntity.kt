package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_activity")
data class ReadingActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storyId: Int,
    val storyTitle: String,
    val quizScore: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)
