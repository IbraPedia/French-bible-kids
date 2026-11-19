package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BibleStory
import com.example.model.StreakRewardResult
import com.example.ui.components.TactileButton
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun QuizCelebrationCard(
    story: BibleStory,
    score: Int,
    totalQuestions: Int,
    streakReward: StreakRewardResult?,
    onDoneNavigateHome: () -> Unit
) {
    Surface(
        modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = WarmPaper,
        border = BorderStroke(2.5.dp, HoneyGold),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🏆 ${story.emoji} 🎉", fontSize = 48.sp)
            Spacer(Modifier.height(10.dp))
            Text("Bravo champion !", style = MaterialTheme.typography.displayLarge, color = WarmInk)
            Spacer(Modifier.height(8.dp))
            Text("Score du Quiz : $score / $totalQuestions ⭐", style = MaterialTheme.typography.titleLarge, color = MeadowGreenDark)
            Spacer(Modifier.height(12.dp))
            val baseXp = 30 + score * 10
            val bonus = streakReward?.bonusXpAwarded ?: 0
            Surface(shape = RoundedCornerShape(18.dp), color = HoneyGold.copy(alpha = 0.25f)) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+$baseXp XP gagnés ! ✨", style = MaterialTheme.typography.titleMedium, color = WarmInk)
                    if (bonus > 0) {
                        Text(
                            text = "🔥 Bonus Série Quotidienne (${streakReward?.newStreak}j) : +$bonus XP !",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFD97706),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            TactileButton(
                onClick = onDoneNavigateHome,
                containerColor = MeadowGreen,
                shadowColor = MeadowGreenDark,
                contentColor = Color.White,
                testTag = "quiz_done_home_button"
            ) {
                Text("Retour aux histoires 🏡", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
