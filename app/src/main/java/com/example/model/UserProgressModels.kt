package com.example.model

data class UserPreferences(
    val childName: String = "Petit Explorateur",
    val avatarEmoji: String = "🦁",
    val textSizeScale: Float = 1.0f,
    val dyslexiaFriendly: Boolean = false,
    val ttsSpeechRate: Float = 0.95f,
    val ttsPitch: Float = 1.05f,
    val ttsAutoAdvance: Boolean = true,
    val difficulty: ReadingDifficulty = ReadingDifficulty.MOYEN,
    val dailyGoal: Int = 2,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastReadEpochDay: Long = -1L,
    val totalXp: Int = 0,
    val storiesReadToday: Int = 0,
    val unlockedEmojis: Set<String> = setOf("🌟", "🕊️", "📖")
) {
    val level: Int
        get() = (totalXp / 150) + 1

    val xpInCurrentLevel: Int
        get() = totalXp % 150

    val levelTitleFr: String
        get() = when {
            level >= 15 -> "Gardien de la Lumière"
            level >= 10 -> "Scribe Royal"
            level >= 6 -> "Ami des Prophètes"
            level >= 3 -> "Explorateur Fidèle"
            else -> "Jeune Voyageur"
        }
}

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val accentHex: Long,
    val isUnlocked: Boolean,
    val progressLabel: String,
    val progressRatio: Float,
    val categoryLabel: String
)

data class StreakRewardResult(
    val newStreak: Int,
    val bonusXpAwarded: Int,
    val isNewDayStreak: Boolean
)
