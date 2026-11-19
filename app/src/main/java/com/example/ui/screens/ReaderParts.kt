package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BibleStory
import com.example.model.ReadingDifficulty
import com.example.ui.components.TactileButton
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk

@Composable
fun ReaderTopBar(
    story: BibleStory,
    difficulty: ReadingDifficulty,
    progress: Float,
    remainingPercent: Int,
    onBack: () -> Unit
) {
    Surface(color = ParchmentCream, shadowElevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = WarmInk)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${story.emoji} ${story.title}",
                        style = MaterialTheme.typography.titleMedium,
                        color = WarmInk,
                        maxLines = 1
                    )
                    Text(
                        text = "${story.reference} • Niveau ${difficulty.labelFr}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = if (remainingPercent == 0) "Terminé ! 🎉" else "Reste $remainingPercent%",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MeadowGreenDark
                )
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .testTag("reader_progress_bar"),
                color = MeadowGreen,
                trackColor = OutlineSand,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun StoryMoralCard(
    story: BibleStory,
    onStartQuiz: () -> Unit
) {
    Surface(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 18.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFE8F5E9),
        border = BorderStroke(2.dp, MeadowGreen.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("🌟 Trésor du Cœur", style = MaterialTheme.typography.titleMedium, color = MeadowGreenDark)
            Spacer(Modifier.height(6.dp))
            Text(story.moral, style = MaterialTheme.typography.bodyLarge, color = WarmInk)
            Spacer(Modifier.height(14.dp))
            TactileButton(
                onClick = onStartQuiz,
                containerColor = MeadowGreen,
                shadowColor = MeadowGreenDark,
                contentColor = Color.White,
                testTag = "finish_story_quiz_button"
            ) {
                Text("Répondre aux ${story.questions.size} questions 🎯", fontSize = 16.sp)
            }
        }
    }
}

fun readerParagraphItemIndex(pIndex: Int, totalParagraphs: Int, scenesCount: Int): Int {
    var idx = 0
    for (i in 0..pIndex) {
        val sIdx = (i * scenesCount) / totalParagraphs
        val showScene = i == 0 || sIdx != ((i - 1) * scenesCount) / totalParagraphs
        if (showScene && sIdx in 0 until scenesCount) idx++
        if (i < pIndex) idx++
    }
    return idx
}
