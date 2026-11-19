package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StoryProgressEntity
import com.example.model.BibleStory
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun StoryPathNode(
    story: BibleStory,
    indexInModule: Int,
    moduleColorHex: Long,
    progress: StoryProgressEntity?,
    isNextSuggested: Boolean,
    onStoryClick: (Int) -> Unit
) {
    val isDone = progress?.isRead == true
    val accent = Color(moduleColorHex)
    val horizontalShift = when (indexInModule % 4) {
        0 -> 0.dp
        1 -> 44.dp
        2 -> 0.dp
        else -> (-44).dp
    }

    val pulseTransition = rememberInfiniteTransition(label = "node_pulse_${story.id}")
    val pulse by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isNextSuggested) 1.08f else 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .offset(x = horizontalShift),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isNextSuggested) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = HoneyGold,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "À LIRE ENSUITE ✨",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = WarmInk,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.scale(pulse)) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .offset(y = 6.dp)
                    .clip(CircleShape)
                    .background(if (isDone) Color(0xFF2E7D32) else accent.copy(alpha = 0.7f))
            )
            Surface(
                modifier = Modifier
                    .size(76.dp)
                    .testTag("story_node_${story.id}")
                    .clip(CircleShape)
                    .clickable { onStoryClick(story.id) },
                shape = CircleShape,
                color = if (isDone) MeadowGreen else if (isNextSuggested) HoneyGold else WarmPaper,
                border = BorderStroke(3.dp, if (isDone) Color.White else accent)
            ) {
                Box(contentAlignment = Alignment.Center) { Text(text = story.emoji, fontSize = 30.sp) }
            }
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).size(26.dp),
                shape = CircleShape,
                color = if (isDone) Color.White else accent,
                border = BorderStroke(1.5.dp, OutlineSand)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isDone) {
                        Icon(Icons.Filled.Check, contentDescription = "Terminé", tint = MeadowGreen, modifier = Modifier.size(16.dp))
                    } else if (isNextSuggested) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Commencer", tint = Color.White, modifier = Modifier.size(16.dp))
                    } else {
                        Text("${story.id}", style = MaterialTheme.typography.labelSmall, color = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            text = story.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = WarmInk,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(190.dp)
        )
        if (progress?.quizCompleted == true) {
            Text(
                text = "Quiz ${progress.bestQuizScore}/${progress.totalQuestions} ⭐",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF2E7D32)
            )
        }
    }
}
