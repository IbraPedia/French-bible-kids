package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.model.QuizQuestion
import com.example.model.ReadingDifficulty
import com.example.ui.components.TactileButton
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun QuizQuestionView(
    storyTitle: String,
    question: QuizQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    difficulty: ReadingDifficulty,
    selectedOption: Int?,
    onSelectOption: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.widthIn(max = 580.dp)) {
        Text(
            text = "Question ${questionIndex + 1} sur $totalQuestions",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WarmPaper,
            border = BorderStroke(2.dp, OutlineSand),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineMedium,
                color = WarmInk,
                modifier = Modifier.padding(20.dp)
            )
        }
        if (difficulty == ReadingDifficulty.FACILE && selectedOption == null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "💡 Indice : Pense bien au cœur de l'histoire « $storyTitle » !",
                style = MaterialTheme.typography.labelSmall,
                color = MeadowGreenDark
            )
        }
        Spacer(Modifier.height(16.dp))
        question.options.forEachIndexed { optIdx, optionText ->
            val isAnswered = selectedOption != null
            val isCorrect = optIdx == question.correctIndex
            val isChosen = optIdx == selectedOption
            val bgColor = when {
                !isAnswered -> WarmPaper
                isCorrect -> Color(0xFFDCFCE7)
                isChosen -> Color(0xFFFFE4E6)
                else -> WarmPaper
            }
            val borderColor = when {
                !isAnswered -> OutlineSand
                isCorrect -> MeadowGreen
                isChosen -> CoralBerry
                else -> OutlineSand
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("quiz_option_$optIdx")
                    .clickable(enabled = !isAnswered) { onSelectOption(optIdx) },
                shape = RoundedCornerShape(18.dp),
                color = bgColor,
                border = BorderStroke(2.dp, borderColor),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = WarmInk,
                        modifier = Modifier.weight(1f)
                    )
                    if (isAnswered && isCorrect) Text("✅", fontSize = 20.sp)
                    else if (isAnswered && isChosen) Text("❌", fontSize = 20.sp)
                }
            }
        }
        AnimatedVisibility(visible = selectedOption != null) {
            Column(modifier = Modifier.padding(top = 14.dp)) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE0F2FE),
                    border = BorderStroke(1.5.dp, Color(0xFF38BDF8))
                ) {
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmInk,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
                TactileButton(
                    onClick = onNext,
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    contentColor = Color.White,
                    testTag = "quiz_next_button"
                ) {
                    Text(
                        text = if (questionIndex + 1 < totalQuestions) "Question suivante →" else "Voir mes récompenses 🏆",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
