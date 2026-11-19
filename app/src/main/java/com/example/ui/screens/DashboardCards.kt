package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserPreferences
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun StreakAndXpBanner(prefs: UserPreferences) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFFFF3CD),
        border = BorderStroke(2.dp, HoneyGold)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("🔥 Série : ${prefs.currentStreak} jour(s)", style = MaterialTheme.typography.titleLarge, color = WarmInk)
                    Text(
                        text = "Record : ${prefs.longestStreak} j • +50 XP par nouveau jour !",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(shape = RoundedCornerShape(14.dp), color = Color.White) {
                    Text(
                        text = "Niv. ${prefs.level}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFD97706),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "${prefs.levelTitleFr} • ${prefs.totalXp} XP au total",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = WarmInk
            )
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (prefs.xpInCurrentLevel / 150f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50)),
                color = HoneyGold,
                trackColor = Color.White,
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Objectif du jour : ${prefs.storiesReadToday} / ${prefs.dailyGoal} histoires lues aujourd'hui",
                style = MaterialTheme.typography.labelSmall,
                color = MeadowGreenDark
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiRewardChestCard(emojis: Set<String>) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = WarmPaper,
        border = BorderStroke(1.5.dp, OutlineSand)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎁 Mon Coffre d'Émojis Gagnés", style = MaterialTheme.typography.titleMedium, color = WarmInk)
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                emojis.forEach { emoji ->
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.5.dp, HoneyGold),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text(emoji, fontSize = 22.sp) }
                    }
                }
            }
        }
    }
}

@Composable
fun StatMiniCard(emoji: String, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = WarmPaper,
        border = BorderStroke(1.5.dp, OutlineSand)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = WarmInk)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
