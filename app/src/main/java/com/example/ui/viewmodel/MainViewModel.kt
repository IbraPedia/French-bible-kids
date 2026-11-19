package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.BibleTtsManager
import com.example.data.local.BibleKidsDatabase
import com.example.data.local.ReadingActivityEntity
import com.example.data.local.StoryCatalogLoader
import com.example.data.local.StoryModuleCatalog
import com.example.data.local.StoryProgressEntity
import com.example.data.local.UserPreferencesStore
import com.example.model.BadgeItem
import com.example.model.BibleStory
import com.example.model.ReadingDifficulty
import com.example.model.StoryModule
import com.example.model.StreakRewardResult
import com.example.model.Testament
import com.example.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = true,
    val stories: List<BibleStory> = emptyList(),
    val modules: List<StoryModule> = StoryModuleCatalog.modules,
    val preferences: UserPreferences = UserPreferences(),
    val progressMap: Map<Int, StoryProgressEntity> = emptyMap(),
    val recentActivities: List<ReadingActivityEntity> = emptyList(),
    val badges: List<BadgeItem> = emptyList(),
    val selectedTestament: Testament? = null,
    val searchQuery: String = "",
    val lastStreakReward: StreakRewardResult? = null
) {
    val completedStoriesCount: Int
        get() = progressMap.values.count { it.isRead }

    val nextSuggestedStoryId: Int
        get() = stories.firstOrNull { progressMap[it.id]?.isRead != true }?.id ?: 1
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsStore = UserPreferencesStore(application)
    private val dao = BibleKidsDatabase.getInstance(application).storyProgressDao()
    private val loader = StoryCatalogLoader(application)
    val ttsManager = BibleTtsManager(application)

    private val storiesFlow = MutableStateFlow<List<BibleStory>>(emptyList())
    private val selectedTestamentFlow = MutableStateFlow<Testament?>(null)
    private val searchQueryFlow = MutableStateFlow("")
    private val lastStreakRewardFlow = MutableStateFlow<StreakRewardResult?>(null)

    val uiState: StateFlow<MainUiState> = combine(
        storiesFlow,
        prefsStore.userPreferencesFlow,
        dao.observeAllProgress(),
        dao.observeRecentActivity(),
        selectedTestamentFlow,
        searchQueryFlow,
        lastStreakRewardFlow
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val stories = values[0] as List<BibleStory>
        val prefs = values[1] as UserPreferences
        @Suppress("UNCHECKED_CAST")
        val progressList = values[2] as List<StoryProgressEntity>
        @Suppress("UNCHECKED_CAST")
        val activities = values[3] as List<ReadingActivityEntity>
        val selectedTestament = values[4] as Testament?
        val query = values[5] as String
        val streakReward = values[6] as StreakRewardResult?

        val progressMap = progressList.associateBy { it.storyId }
        val badges = StoryModuleCatalog.buildBadges(stories, progressMap, prefs)

        MainUiState(
            isLoading = stories.isEmpty(),
            stories = stories,
            preferences = prefs,
            progressMap = progressMap,
            recentActivities = activities,
            badges = badges,
            selectedTestament = selectedTestament,
            searchQuery = query,
            lastStreakReward = streakReward
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState())

    init {
        viewModelScope.launch {
            storiesFlow.value = loader.loadAllStories()
        }
    }

    fun selectTestament(testament: Testament?) {
        selectedTestamentFlow.value = testament
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun updateChildProfile(name: String, avatarEmoji: String) {
        viewModelScope.launch { prefsStore.updateChildProfile(name, avatarEmoji) }
    }

    fun updateAccessibility(
        textScale: Float,
        dyslexiaFriendly: Boolean,
        ttsRate: Float,
        ttsPitch: Float,
        ttsAutoAdvance: Boolean
    ) {
        viewModelScope.launch {
            prefsStore.updateAccessibility(textScale, dyslexiaFriendly, ttsRate, ttsPitch, ttsAutoAdvance)
        }
    }

    fun updateParentalSettings(difficulty: ReadingDifficulty, dailyGoal: Int) {
        viewModelScope.launch { prefsStore.updateParentalSettings(difficulty, dailyGoal) }
    }

    fun recordQuizAndReadingCompletion(story: BibleStory, score: Int, totalQuestions: Int) {
        viewModelScope.launch {
            val existing = dao.getByStoryId(story.id)
            val bestScore = maxOf(existing?.bestQuizScore ?: 0, score)
            val xpEarned = 30 + (score * 10)
            dao.upsertProgress(
                StoryProgressEntity(
                    storyId = story.id,
                    isRead = true,
                    quizCompleted = true,
                    bestQuizScore = bestScore,
                    totalQuestions = totalQuestions,
                    attemptsCount = (existing?.attemptsCount ?: 0) + 1,
                    lastReadTimestamp = System.currentTimeMillis(),
                    readingSeconds = (existing?.readingSeconds ?: 0) + (story.durationMinutes * 60)
                )
            )
            dao.insertActivity(
                ReadingActivityEntity(
                    storyId = story.id,
                    storyTitle = story.title,
                    quizScore = score,
                    totalQuestions = totalQuestions,
                    xpEarned = xpEarned
                )
            )
            lastStreakRewardFlow.value = prefsStore.recordCompletionAndStreak(xpEarned, story.emoji)
        }
    }

    fun clearStreakBanner() {
        lastStreakRewardFlow.value = null
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            dao.clearProgress()
            dao.clearActivity()
            prefsStore.resetAll()
        }
    }

    override fun onCleared() {
        ttsManager.shutdown()
        super.onCleared()
    }
}
