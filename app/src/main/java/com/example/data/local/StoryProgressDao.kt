package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryProgressDao {
    @Query("SELECT * FROM story_progress")
    fun observeAllProgress(): Flow<List<StoryProgressEntity>>

    @Query("SELECT * FROM story_progress WHERE storyId = :storyId LIMIT 1")
    suspend fun getByStoryId(storyId: Int): StoryProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: StoryProgressEntity)

    @Query("SELECT * FROM reading_activity ORDER BY timestamp DESC LIMIT 30")
    fun observeRecentActivity(): Flow<List<ReadingActivityEntity>>

    @Insert
    suspend fun insertActivity(activity: ReadingActivityEntity)

    @Query("DELETE FROM story_progress")
    suspend fun clearProgress()

    @Query("DELETE FROM reading_activity")
    suspend fun clearActivity()
}
