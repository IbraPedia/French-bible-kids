package com.example.data.local

import com.example.model.BadgeItem
import com.example.model.BibleStory
import com.example.model.StoryModule
import com.example.model.Testament
import com.example.model.UserPreferences

object StoryModuleCatalog {
    val modules: List<StoryModule> = listOf(
        StoryModule(1, "L'Aube du Monde", "Création, Noé & Abraham", Testament.ANCIEN_TESTAMENT, "🌿", "Gardien du Jardin", "🌈", 0xFF4CAF50),
        StoryModule(2, "Patriarches & Joseph", "Rêves, Courage & Pardon", Testament.ANCIEN_TESTAMENT, "👑", "Étoile de Joseph", "🧥", 0xFFF9A826),
        StoryModule(3, "Moïse & La Liberté", "Du Nil au Mont Sinaï", Testament.ANCIEN_TESTAMENT, "🔥", "Flamme de Liberté", "🌊", 0xFF38BDF8),
        StoryModule(4, "Juges & Rois", "David, Ruth & Salomon", Testament.ANCIEN_TESTAMENT, "🛡️", "Couronne de David", "🎵", 0xFFA78BFA),
        StoryModule(5, "Prophètes & Héros", "Daniel, Esther & Jonas", Testament.ANCIEN_TESTAMENT, "🦁", "Cœur de Lion", "🐋", 0xFFFB7185),
        StoryModule(6, "Noël & Enfance", "Bethléem & Les Anges", Testament.NOUVEAU_TESTAMENT, "⭐", "Étoile de Bethléem", "👼", 0xFFF59E0B),
        StoryModule(7, "Miracles de Jésus", "Pains, Tempête & Joie", Testament.NOUVEAU_TESTAMENT, "✨", "Main Bienveillante", "⛵", 0xFF0EA5E9),
        StoryModule(8, "Paraboles du Cœur", "Berger, Trésor & Amour", Testament.NOUVEAU_TESTAMENT, "💛", "Brebis Retrouvée", "🐑", 0xFFEC4899),
        StoryModule(9, "Amis de Pâques", "Zachée, Croix & Matin", Testament.NOUVEAU_TESTAMENT, "🕊️", "Colombe de Pâques", "🌅", 0xFF10B981),
        StoryModule(10, "L'Église & L'Espoir", "Pierre, Paul & Paix", Testament.NOUVEAU_TESTAMENT, "🌍", "Messager de Paix", "📜", 0xFF8B5CF6)
    )

    fun buildBadges(
        stories: List<BibleStory>,
        progressMap: Map<Int, StoryProgressEntity>,
        prefs: UserPreferences
    ): List<BadgeItem> {
        val moduleBadges = modules.map { module ->
            val modStories = stories.filter { it.moduleId == module.id }
            val completed = modStories.count { progressMap[it.id]?.isRead == true }
            val total = modStories.size.coerceAtLeast(1)
            BadgeItem(
                id = "mod_${module.id}",
                title = module.badgeTitle,
                description = "Termine les 10 histoires de « ${module.title} ».",
                emoji = module.badgeEmoji,
                accentHex = module.colorHex,
                isUnlocked = completed >= total && total > 0,
                progressLabel = "$completed / $total histoires",
                progressRatio = (completed.toFloat() / total).coerceIn(0f, 1f),
                categoryLabel = "Autocollant de Module"
            )
        }

        val totalRead = progressMap.values.count { it.isRead }
        val perfectQuizzes = progressMap.values.count { it.quizCompleted && it.bestQuizScore >= 5 }

        val milestones = listOf(
            BadgeItem(
                id = "first_story",
                title = "Premier Pas",
                description = "Lis ta toute première histoire biblique.",
                emoji = "📖",
                accentHex = 0xFFF9A826,
                isUnlocked = totalRead >= 1,
                progressLabel = "${totalRead.coerceAtMost(1)} / 1 histoire",
                progressRatio = if (totalRead >= 1) 1f else 0f,
                categoryLabel = "Autocollant Spécial"
            ),
            BadgeItem(
                id = "streak_3",
                title = "Flamme Fidèle",
                description = "Lis une histoire 3 jours de suite.",
                emoji = "🔥",
                accentHex = 0xFFFB7185,
                isUnlocked = prefs.longestStreak >= 3,
                progressLabel = "${prefs.longestStreak.coerceAtMost(3)} / 3 jours",
                progressRatio = (prefs.longestStreak / 3f).coerceIn(0f, 1f),
                categoryLabel = "Autocollant Série"
            ),
            BadgeItem(
                id = "streak_7",
                title = "Semaine Lumineuse",
                description = "Garde ta série active pendant 7 jours !",
                emoji = "🌞",
                accentHex = 0xFFF59E0B,
                isUnlocked = prefs.longestStreak >= 7,
                progressLabel = "${prefs.longestStreak.coerceAtMost(7)} / 7 jours",
                progressRatio = (prefs.longestStreak / 7f).coerceIn(0f, 1f),
                categoryLabel = "Autocollant Série"
            ),
            BadgeItem(
                id = "quiz_master",
                title = "Champion du Quiz",
                description = "Obtiens 5/5 à au moins 5 quiz différents.",
                emoji = "🎯",
                accentHex = 0xFF0EA5E9,
                isUnlocked = perfectQuizzes >= 5,
                progressLabel = "${perfectQuizzes.coerceAtMost(5)} / 5 sans-faute",
                progressRatio = (perfectQuizzes / 5f).coerceIn(0f, 1f),
                categoryLabel = "Autocollant Savoir"
            ),
            BadgeItem(
                id = "bible_50",
                title = "Grand Explorateur",
                description = "Découvre la moitié du livre : 50 histoires lues.",
                emoji = "🗺️",
                accentHex = 0xFF10B981,
                isUnlocked = totalRead >= 50,
                progressLabel = "${totalRead.coerceAtMost(50)} / 50 histoires",
                progressRatio = (totalRead / 50f).coerceIn(0f, 1f),
                categoryLabel = "Autocollant Spécial"
            ),
            BadgeItem(
                id = "bible_100",
                title = "Couronne des 100 Histoires",
                description = "Termine toutes les 100 histoires de la Bible !",
                emoji = "🏆",
                accentHex = 0xFF8B5CF6,
                isUnlocked = totalRead >= 100,
                progressLabel = "$totalRead / 100 histoires",
                progressRatio = (totalRead / 100f).coerceIn(0f, 1f),
                categoryLabel = "Grand Trésor"
            )
        )
        return moduleBadges + milestones
    }
}
