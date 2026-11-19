package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [StoryProgressEntity::class, ReadingActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BibleKidsDatabase : RoomDatabase() {
    abstract fun storyProgressDao(): StoryProgressDao

    companion object {
        @Volatile
        private var INSTANCE: BibleKidsDatabase? = null

        fun getInstance(context: Context): BibleKidsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BibleKidsDatabase::class.java,
                    "bible_kids_offline.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
