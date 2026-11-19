package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.ReadingDifficulty
import com.example.model.Testament
import com.example.ui.components.TactileButton
import com.example.ui.theme.CoralDark
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper
import com.example.ui.viewmodel.MainUiState

@Composable
fun ParentalGateCard(
    gateAnswer: String,
    gateError: Boolean,
    onAnswerChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(ParchmentCream).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = WarmPaper,
            border = BorderStroke(2.dp, OutlineSand),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛡️ Espace Parents", style = MaterialTheme.typography.headlineMedium, color = WarmInk)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Pour accéder aux rapports et réglages, résolvez cette opération :",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(14.dp))
                Text("Combien font  7 × 6 ?", style = MaterialTheme.typography.titleLarge, color = WarmInk)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = gateAnswer,
                    onValueChange = onAnswerChange,
                    label = { Text("Réponse") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = gateError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("parental_gate_input")
                )
                if (gateError) {
                    Text("Réponse incorrecte, réessayez.", color = CoralDark, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.height(14.dp))
                TactileButton(
                    onClick = onSubmit,
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    contentColor = Color.White,
                    testTag = "parental_gate_unlock"
                ) {
                    Text("Déverrouiller l'Espace Parents 🔓")
                }
            }
        }
    }
}

@Composable
fun ParentalProgressReportCard(uiState: MainUiState) {
    val prefs = uiState.preferences
    val quizResults = uiState.progressMap.values.filter { it.quizCompleted }
    val avgPercent = if (quizResults.isEmpty()) 0 else {
        quizResults.sumOf { (it.bestQuizScore * 100) / it.totalQuestions.coerceAtLeast(1) } / quizResults.size
    }
    val totalMinutes = uiState.progressMap.values.sumOf { it.readingSeconds } / 60
    val otDone = uiState.stories.count { it.testament == Testament.ANCIEN_TESTAMENT && uiState.progressMap[it.id]?.isRead == true }
    val ntDone = uiState.stories.count { it.testament == Testament.NOUVEAU_TESTAMENT && uiState.progressMap[it.id]?.isRead == true }

    Surface(shape = RoundedCornerShape(22.dp), color = WarmPaper, border = BorderStroke(1.5.dp, OutlineSand)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📊 Rapport de Progrès de ${prefs.childName}", style = MaterialTheme.typography.titleMedium, color = WarmInk)
            Spacer(Modifier.height(8.dp))
            Text("• Histoires terminées : ${uiState.completedStoriesCount} / 100", style = MaterialTheme.typography.bodyMedium)
            Text("• Ancien Testament : $otDone / 50  •  Nouveau Testament : $ntDone / 50", style = MaterialTheme.typography.bodyMedium)
            Text("• Moyenne aux quiz : $avgPercent%  (${quizResults.size} quiz complétés)", style = MaterialTheme.typography.bodyMedium)
            Text("• Temps de lecture estimé : $totalMinutes min  •  Série max : ${prefs.longestStreak} j", style = MaterialTheme.typography.bodyMedium)
            Text("• Mode hors ligne : Actif (100% local, sans publicité)", style = MaterialTheme.typography.labelSmall, color = MeadowGreenDark)
        }
    }
}

@Composable
fun ParentalDifficultyCard(
    currentDifficulty: ReadingDifficulty,
    dailyGoal: Int,
    onUpdateSettings: (ReadingDifficulty, Int) -> Unit
) {
    Surface(shape = RoundedCornerShape(22.dp), color = WarmPaper, border = BorderStroke(1.5.dp, OutlineSand)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎚️ Niveau de difficulté de lecture", style = MaterialTheme.typography.titleMedium, color = WarmInk)
            Spacer(Modifier.height(6.dp))
            ReadingDifficulty.entries.forEach { diff ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onUpdateSettings(diff, dailyGoal) }.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = currentDifficulty == diff, onClick = { onUpdateSettings(diff, dailyGoal) })
                    Column {
                        Text("${diff.labelFr} (${diff.ageHint})", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        Text(diff.descriptionFr, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("🎯 Objectif quotidien : $dailyGoal histoire(s) / jour", style = MaterialTheme.typography.bodyLarge)
            Slider(value = dailyGoal.toFloat(), onValueChange = { onUpdateSettings(currentDifficulty, it.toInt()) }, valueRange = 1f..5f, steps = 3)
        }
    }
}
