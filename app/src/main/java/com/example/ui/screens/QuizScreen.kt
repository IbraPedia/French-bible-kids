package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.model.BibleStory
import com.example.model.ReadingDifficulty
import com.example.model.StreakRewardResult
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk

@Composable
fun QuizScreen(
    story: BibleStory,
    difficulty: ReadingDifficulty,
    streakReward: StreakRewardResult?,
    onQuizFinished: (score: Int, total: Int) -> Unit,
    onDoneNavigateHome: () -> Unit,
    onBackToStory: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    BackHandler { onBackToStory() }

    val questions = story.questions
    val currentQ = questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentCream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackToStory) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = WarmInk)
            }
            Text(
                text = "Quiz : ${story.title} ${story.emoji}",
                style = MaterialTheme.typography.titleMedium,
                color = WarmInk,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { if (isCompleted) 1f else (currentIndex + 1).toFloat() / questions.size.coerceAtLeast(1) },
            modifier = Modifier
                .widthIn(max = 580.dp)
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50)),
            color = HoneyGold,
            trackColor = OutlineSand,
            strokeCap = StrokeCap.Round
        )

        Spacer(Modifier.height(22.dp))

        if (!isCompleted && currentQ != null) {
            QuizQuestionView(
                storyTitle = story.title,
                question = currentQ,
                questionIndex = currentIndex,
                totalQuestions = questions.size,
                difficulty = difficulty,
                selectedOption = selectedOption,
                onSelectOption = { optIdx ->
                    selectedOption = optIdx
                    if (optIdx == currentQ.correctIndex) score++
                },
                onNext = {
                    if (currentIndex + 1 < questions.size) {
                        currentIndex++
                        selectedOption = null
                    } else {
                        isCompleted = true
                        onQuizFinished(score, questions.size)
                    }
                }
            )
        } else {
            QuizCelebrationCard(
                story = story,
                score = score,
                totalQuestions = questions.size,
                streakReward = streakReward,
                onDoneNavigateHome = onDoneNavigateHome
            )
        }
    }
}
