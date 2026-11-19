package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ReadingDifficulty
import com.example.ui.components.TactileButton
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CoralDark
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper
import com.example.ui.viewmodel.MainUiState
import com.example.util.DateHelper

@Composable
fun ParentalScreen(
    uiState: MainUiState,
    onUpdateParentalSettings: (ReadingDifficulty, Int) -> Unit,
    onOpenAccessibility: () -> Unit,
    onResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    var unlocked by remember { mutableStateOf(false) }
    var gateAnswer by remember { mutableStateOf("") }
    var gateError by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    if (!unlocked) {
        ParentalGateCard(
            gateAnswer = gateAnswer,
            gateError = gateError,
            onAnswerChange = {
                gateAnswer = it.filter { ch -> ch.isDigit() }
                gateError = false
            },
            onSubmit = { if (gateAnswer.trim() == "42") unlocked = true else gateError = true },
            modifier = modifier
        )
        return
    }

    val prefs = uiState.preferences

    LazyColumn(
        modifier = modifier.fillMaxSize().background(ParchmentCream),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Rapports & Contrôle Parental 🛡️", style = MaterialTheme.typography.titleLarge, color = WarmInk)
                TextButton(onClick = { unlocked = false }) { Text("Verrouiller 🔒") }
            }
        }
        item { ParentalProgressReportCard(uiState = uiState) }
        item {
            ParentalDifficultyCard(
                currentDifficulty = prefs.difficulty,
                dailyGoal = prefs.dailyGoal,
                onUpdateSettings = onUpdateParentalSettings
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TactileButton(
                    onClick = onOpenAccessibility,
                    containerColor = HoneyGold,
                    shadowColor = Color(0xFFD9820B),
                    contentColor = WarmInk
                ) {
                    Text("Accessibilité : Taille, Dyslexie & Audio 🎧")
                }
                TactileButton(
                    onClick = { showResetConfirm = true },
                    containerColor = CoralBerry,
                    shadowColor = CoralDark,
                    contentColor = Color.White,
                    testTag = "parental_reset_button"
                ) {
                    Text("Réinitialiser la progression 🔄")
                }
            }
        }
        item { Text("🕒 Activités récentes", style = MaterialTheme.typography.titleMedium, color = WarmInk) }
        if (uiState.recentActivities.isEmpty()) {
            item {
                Text(
                    text = "Aucune histoire terminée pour le moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(uiState.recentActivities, key = { it.id }) { act ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WarmPaper,
                    border = BorderStroke(1.dp, OutlineSand),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(act.storyTitle, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                            Text(DateHelper.formatTimestampFr(act.timestamp), style = MaterialTheme.typography.labelSmall)
                        }
                        Text("${act.quizScore}/${act.totalQuestions} ⭐ (+${act.xpEarned} XP)", style = MaterialTheme.typography.labelLarge, color = MeadowGreenDark)
                    }
                }
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Effacer la progression ?") },
            text = { Text("Toutes les histoires lues, séries, XP et badges seront remis à zéro sur cet appareil.") },
            confirmButton = {
                TextButton(onClick = {
                    onResetProgress()
                    showResetConfirm = false
                }) { Text("Confirmer", color = CoralDark) }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) { Text("Annuler") }
            }
        )
    }
}
