package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_progress")
data class StoryProgressEntity(
    @PrimaryKey val storyId: Int,
    val isRead: Boolean = false,
    val quizCompleted: Boolean = false,
    val bestQuizScore: Int = 0,
    val totalQuestions: Int = 5,
    val attemptsCount: Int = 0,
    val lastReadTimestamp: Long = 0L,
    val readingSeconds: Int = 0
)
