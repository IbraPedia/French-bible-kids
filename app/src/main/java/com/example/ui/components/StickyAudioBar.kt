package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk

@Composable
fun StickyAudioBar(
    isSpeaking: Boolean,
    speechRate: Float,
    onTogglePlay: () -> Unit,
    onPrevParagraph: () -> Unit,
    onNextParagraph: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onStartQuiz: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ParchmentCream,
        tonalElevation = 10.dp,
        shadowElevation = 12.dp,
        border = BorderStroke(1.5.dp, OutlineSand),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Accessibility quick-open button
                Surface(
                    onClick = onOpenAccessibility,
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.5.dp, OutlineSand),
                    modifier = Modifier.testTag("reader_accessibility_button")
                ) {
                    Text(
                        text = "Aa",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = WarmInk,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                // Audio playback controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPrevParagraph, modifier = Modifier.testTag("tts_prev")) {
                        Icon(Icons.Filled.SkipPrevious, contentDescription = "Paragraphe précédent", tint = WarmInk)
                    }
                    Surface(
                        onClick = onTogglePlay,
                        shape = CircleShape,
                        color = HoneyGold,
                        modifier = Modifier
                            .size(50.dp)
                            .testTag("tts_play_pause")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isSpeaking) "Pause audio" else "Écouter l'histoire",
                            tint = WarmInk,
                            modifier = Modifier.padding(11.dp)
                        )
                    }
                    IconButton(onClick = onNextParagraph, modifier = Modifier.testTag("tts_next")) {
                        Icon(Icons.Filled.SkipNext, contentDescription = "Paragraphe suivant", tint = WarmInk)
                    }
                }

                // Quiz Launch Tactile CTA
                Surface(
                    onClick = onStartQuiz,
                    shape = RoundedCornerShape(16.dp),
                    color = MeadowGreen,
                    border = BorderStroke(1.5.dp, MeadowGreenDark),
                    modifier = Modifier.testTag("reader_quiz_button")
                ) {
                    Text(
                        text = "Quiz 🎯",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSpeaking) "🔊 Lecture audio en cours..." else "🎧 Écouter en français",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "• ${"%.1fx".format(speechRate)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmInk
                )
            }
        }
    }
}
