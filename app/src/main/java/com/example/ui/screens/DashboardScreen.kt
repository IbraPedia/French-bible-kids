package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TactileButton
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper
import com.example.ui.viewmodel.MainUiState

@Composable
fun DashboardScreen(
    uiState: MainUiState,
    onSaveProfile: (name: String, avatar: String) -> Unit,
    onOpenAccessibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = uiState.preferences
    var nameInput by remember(prefs.childName) { mutableStateOf(prefs.childName) }
    var selectedAvatar by remember(prefs.avatarEmoji) { mutableStateOf(prefs.avatarEmoji) }
    var savedFeedback by remember { mutableStateOf(false) }

    val avatarChoices = listOf("🦁", "🐑", "🕊️", "🌟", "🐳", "🦊")

    LazyColumn(
        modifier = modifier.fillMaxSize().background(ParchmentCream),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Mon Tableau de Progrès 🌟", style = MaterialTheme.typography.headlineMedium, color = WarmInk)
        }
        item { StreakAndXpBanner(prefs = prefs) }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMiniCard("📖", "${uiState.completedStoriesCount}/100", "Histoires lues", Modifier.weight(1f))
                StatMiniCard("🎯", "${uiState.progressMap.values.count { it.quizCompleted }}", "Quiz réussis", Modifier.weight(1f))
                StatMiniCard("🎨", "${uiState.badges.count { it.isUnlocked }}", "Badges", Modifier.weight(1f))
            }
        }
        item { EmojiRewardChestCard(emojis = prefs.unlockedEmojis) }
        item {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = WarmPaper,
                border = BorderStroke(1.5.dp, OutlineSand)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✏️ Personnaliser mon Profil (hors ligne)", style = MaterialTheme.typography.titleMedium, color = WarmInk)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            savedFeedback = false
                        },
                        label = { Text("Ton prénom") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("Choisis ton compagnon :", style = MaterialTheme.typography.labelLarge, color = WarmInk)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        avatarChoices.forEach { emoji ->
                            val selected = selectedAvatar == emoji
                            Surface(
                                shape = CircleShape,
                                color = if (selected) HoneyGold.copy(alpha = 0.35f) else Color.White,
                                border = BorderStroke(if (selected) 2.5.dp else 1.dp, if (selected) HoneyGold else OutlineSand),
                                modifier = Modifier.size(46.dp).clickable {
                                    selectedAvatar = emoji
                                    savedFeedback = false
                                }
                            ) {
                                Box(contentAlignment = Alignment.Center) { Text(emoji, fontSize = 22.sp) }
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    TactileButton(
                        onClick = {
                            onSaveProfile(nameInput, selectedAvatar)
                            savedFeedback = true
                        },
                        containerColor = MeadowGreen,
                        shadowColor = MeadowGreenDark,
                        contentColor = Color.White,
                        testTag = "save_profile_button"
                    ) {
                        Text(if (savedFeedback) "✅ Enregistré !" else "Enregistrer mon prénom 💾")
                    }
                }
            }
        }
        item {
            TactileButton(
                onClick = onOpenAccessibility,
                containerColor = SkyBlue,
                shadowColor = Color(0xFF0284C7),
                contentColor = Color.White,
                testTag = "dashboard_accessibility_button"
            ) {
                Text("Réglages Texte, Dyslexie & Voix Audio 🎧")
            }
        }
    }
}
