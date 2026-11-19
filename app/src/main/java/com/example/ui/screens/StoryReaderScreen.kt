package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.BibleTtsManager
import com.example.model.BibleStory
import com.example.model.ReadingDifficulty
import com.example.model.UserPreferences
import com.example.ui.components.AccessibilitySheet
import com.example.ui.components.StickyAudioBar
import com.example.ui.illustration.WatercolorSceneCard
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper
import kotlinx.coroutines.launch

@Composable
fun StoryReaderScreen(
    story: BibleStory,
    preferences: UserPreferences,
    ttsManager: BibleTtsManager,
    onUpdateAccessibility: (Float, Boolean, Float, Float, Boolean) -> Unit,
    onBack: () -> Unit,
    onStartQuiz: (Int) -> Unit
) {
    val ttsState by ttsManager.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var activeParagraph by remember { mutableIntStateOf(0) }
    var showAccessibility by remember { mutableStateOf(false) }

    DisposableEffect(story.id) { onDispose { ttsManager.stop() } }
    BackHandler {
        ttsManager.stop()
        onBack()
    }

    val totalParagraphs = story.paragraphs.size.coerceAtLeast(1)
    val scrollProgress by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val total = info.totalItemsCount.coerceAtLeast(1)
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            if (total <= 1) 1f else ((lastVisible + 1).toFloat() / total.toFloat()).coerceIn(0.08f, 1f)
        }
    }
    val animatedProgress by animateFloatAsState(targetValue = scrollProgress, label = "reader_progress")
    val remainingPercent = ((1f - animatedProgress) * 100).toInt().coerceAtLeast(0)

    fun speakAt(index: Int) {
        if (index !in story.paragraphs.indices) return
        activeParagraph = index
        val targetItem = readerParagraphItemIndex(index, totalParagraphs, story.scenes.size)
        scope.launch { listState.animateScrollToItem(targetItem) }
        ttsManager.speakParagraph(
            text = story.paragraphs[index],
            paragraphIndex = index,
            speechRate = preferences.ttsSpeechRate,
            pitch = preferences.ttsPitch
        ) { finishedIdx ->
            if (preferences.ttsAutoAdvance && finishedIdx + 1 < story.paragraphs.size) {
                scope.launch { speakAt(finishedIdx + 1) }
            }
        }
    }

    Scaffold(
        containerColor = ParchmentCream,
        topBar = {
            ReaderTopBar(
                story = story,
                difficulty = preferences.difficulty,
                progress = animatedProgress,
                remainingPercent = remainingPercent,
                onBack = {
                    ttsManager.stop()
                    onBack()
                }
            )
        },
        bottomBar = {
            StickyAudioBar(
                isSpeaking = ttsState.isSpeaking,
                speechRate = preferences.ttsSpeechRate,
                onTogglePlay = { if (ttsState.isSpeaking) ttsManager.stop() else speakAt(activeParagraph) },
                onPrevParagraph = { speakAt((activeParagraph - 1).coerceAtLeast(0)) },
                onNextParagraph = { speakAt((activeParagraph + 1).coerceAtMost(totalParagraphs - 1)) },
                onOpenAccessibility = { showAccessibility = true },
                onStartQuiz = {
                    ttsManager.stop()
                    onStartQuiz(story.id)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(ParchmentCream),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            story.paragraphs.forEachIndexed { pIndex, paragraph ->
                val sceneIndex = (pIndex * story.scenes.size) / totalParagraphs
                val showSceneBefore = pIndex == 0 ||
                    sceneIndex != ((pIndex - 1) * story.scenes.size) / totalParagraphs

                if (showSceneBefore && sceneIndex in story.scenes.indices) {
                    val itemKey = "scene_$sceneIndex"
                    item(key = itemKey) {
                        val visibleInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == itemKey }
                        val parallaxPx = ((visibleInfo?.offset ?: 0) * -0.08f).coerceIn(-24f, 24f)
                        WatercolorSceneCard(
                            scene = story.scenes[sceneIndex],
                            sceneNumber = sceneIndex + 1,
                            totalScenes = story.scenes.size,
                            parallaxOffsetPx = parallaxPx,
                            modifier = Modifier.widthIn(max = 600.dp).padding(bottom = 12.dp)
                        )
                    }
                }

                item(key = "para_$pIndex") {
                    val isActive = ttsState.isSpeaking && activeParagraph == pIndex
                    val extraGap = if (preferences.difficulty == ReadingDifficulty.FACILE) 16.dp else 12.dp
                    Surface(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .padding(bottom = extraGap)
                            .clickable { speakAt(pIndex) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isActive) HoneyGold.copy(alpha = 0.22f) else WarmPaper,
                        border = BorderStroke(if (isActive) 2.dp else 1.dp, if (isActive) HoneyGold else OutlineSand)
                    ) {
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge,
                            color = WarmInk,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            item(key = "moral_end") {
                StoryMoralCard(
                    story = story,
                    onStartQuiz = {
                        ttsManager.stop()
                        onStartQuiz(story.id)
                    }
                )
            }
        }
    }

    if (showAccessibility) {
        AccessibilitySheet(
            preferences = preferences,
            onUpdateAccessibility = onUpdateAccessibility,
            onTestFrenchVoice = { ttsManager.speakSample(preferences.ttsSpeechRate, preferences.ttsPitch) },
            onDismiss = { showAccessibility = false }
        )
    }
}
