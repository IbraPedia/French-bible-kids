package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.ReadingDifficulty
import com.example.model.StreakRewardResult
import com.example.model.UserPreferences
import com.example.util.DateHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "bible_kids_preferences"
)

class UserPreferencesStore(private val context: Context) {

    private object Keys {
        val CHILD_NAME = stringPreferencesKey("child_name")
        val AVATAR_EMOJI = stringPreferencesKey("avatar_emoji")
        val TEXT_SIZE_SCALE = floatPreferencesKey("text_size_scale")
        val DYSLEXIA_FRIENDLY = booleanPreferencesKey("dyslexia_friendly")
        val TTS_SPEECH_RATE = floatPreferencesKey("tts_speech_rate")
        val TTS_PITCH = floatPreferencesKey("tts_pitch")
        val TTS_AUTO_ADVANCE = booleanPreferencesKey("tts_auto_advance")
        val DIFFICULTY = stringPreferencesKey("difficulty")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
        val LAST_READ_EPOCH_DAY = longPreferencesKey("last_read_epoch_day")
        val TOTAL_XP = intPreferencesKey("total_xp")
        val STORIES_READ_TODAY = intPreferencesKey("stories_read_today")
        val UNLOCKED_EMOJIS = stringSetPreferencesKey("unlocked_emojis")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val today = DateHelper.todayEpochDay()
        val lastDay = prefs[Keys.LAST_READ_EPOCH_DAY] ?: -1L
        val rawStreak = prefs[Keys.CURRENT_STREAK] ?: 0
        val activeStreak = if (lastDay == today || lastDay == today - 1L) rawStreak else 0
        val readToday = if (lastDay == today) (prefs[Keys.STORIES_READ_TODAY] ?: 0) else 0
        val diffName = prefs[Keys.DIFFICULTY] ?: ReadingDifficulty.MOYEN.name
        val difficulty = ReadingDifficulty.entries.find { it.name == diffName }
            ?: ReadingDifficulty.MOYEN

        UserPreferences(
            childName = prefs[Keys.CHILD_NAME] ?: "Petit Explorateur",
            avatarEmoji = prefs[Keys.AVATAR_EMOJI] ?: "🦁",
            textSizeScale = prefs[Keys.TEXT_SIZE_SCALE] ?: 1.0f,
            dyslexiaFriendly = prefs[Keys.DYSLEXIA_FRIENDLY] ?: false,
            ttsSpeechRate = prefs[Keys.TTS_SPEECH_RATE] ?: 0.95f,
            ttsPitch = prefs[Keys.TTS_PITCH] ?: 1.05f,
            ttsAutoAdvance = prefs[Keys.TTS_AUTO_ADVANCE] ?: true,
            difficulty = difficulty,
            dailyGoal = prefs[Keys.DAILY_GOAL] ?: 2,
            currentStreak = activeStreak,
            longestStreak = prefs[Keys.LONGEST_STREAK] ?: 0,
            lastReadEpochDay = lastDay,
            totalXp = prefs[Keys.TOTAL_XP] ?: 0,
            storiesReadToday = readToday,
            unlockedEmojis = prefs[Keys.UNLOCKED_EMOJIS] ?: setOf("🌟", "🕊️", "📖")
        )
    }

    suspend fun updateChildProfile(name: String, avatarEmoji: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CHILD_NAME] = name.trim().ifEmpty { "Petit Explorateur" }
            prefs[Keys.AVATAR_EMOJI] = avatarEmoji
        }
    }

    suspend fun updateAccessibility(
        textScale: Float,
        dyslexiaFriendly: Boolean,
        ttsRate: Float,
        ttsPitch: Float,
        ttsAutoAdvance: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TEXT_SIZE_SCALE] = textScale.coerceIn(0.85f, 1.45f)
            prefs[Keys.DYSLEXIA_FRIENDLY] = dyslexiaFriendly
            prefs[Keys.TTS_SPEECH_RATE] = ttsRate.coerceIn(0.6f, 1.4f)
            prefs[Keys.TTS_PITCH] = ttsPitch.coerceIn(0.8f, 1.3f)
            prefs[Keys.TTS_AUTO_ADVANCE] = ttsAutoAdvance
        }
    }

    suspend fun updateParentalSettings(difficulty: ReadingDifficulty, dailyGoal: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DIFFICULTY] = difficulty.name
            prefs[Keys.DAILY_GOAL] = dailyGoal.coerceIn(1, 5)
            prefs[Keys.TTS_SPEECH_RATE] = difficulty.defaultTtsRate
        }
    }

    suspend fun recordCompletionAndStreak(
        baseXp: Int,
        storyEmoji: String
    ): StreakRewardResult {
        var result = StreakRewardResult(newStreak = 1, bonusXpAwarded = 25, isNewDayStreak = true)
        context.dataStore.edit { prefs ->
            val today = DateHelper.todayEpochDay()
            val lastDay = prefs[Keys.LAST_READ_EPOCH_DAY] ?: -1L
            val prevStreak = prefs[Keys.CURRENT_STREAK] ?: 0
            val prevLongest = prefs[Keys.LONGEST_STREAK] ?: 0
            val prevXp = prefs[Keys.TOTAL_XP] ?: 0
            val prevTodayCount = if (lastDay == today) (prefs[Keys.STORIES_READ_TODAY] ?: 0) else 0

            val (newStreak, streakBonus, isNewDay) = when (lastDay) {
                today -> Triple(prevStreak.coerceAtLeast(1), 0, false)
                today - 1L -> {
                    val incremented = prevStreak + 1
                    val bonus = if (incremented % 7 == 0) 100 else 50
                    Triple(incremented, bonus, true)
                }
                else -> Triple(1, 25, true)
            }

            prefs[Keys.CURRENT_STREAK] = newStreak
            prefs[Keys.LONGEST_STREAK] = maxOf(prevLongest, newStreak)
            prefs[Keys.LAST_READ_EPOCH_DAY] = today
            prefs[Keys.STORIES_READ_TODAY] = prevTodayCount + 1
            prefs[Keys.TOTAL_XP] = prevXp + baseXp + streakBonus

            val emojis = (prefs[Keys.UNLOCKED_EMOJIS] ?: setOf("🌟", "🕊️", "📖")).toMutableSet()
            emojis.add(storyEmoji)
            if (newStreak >= 3) emojis.add("🔥")
            if (newStreak >= 7) emojis.add("🏆")
            prefs[Keys.UNLOCKED_EMOJIS] = emojis

            result = StreakRewardResult(
                newStreak = newStreak,
                bonusXpAwarded = streakBonus,
                isNewDayStreak = isNewDay
            )
        }
        return result
    }

    suspend fun resetAll() {
        context.dataStore.edit { it.clear() }
    }
}
