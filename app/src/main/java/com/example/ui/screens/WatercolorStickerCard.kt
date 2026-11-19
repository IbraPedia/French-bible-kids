package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BadgeItem
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun WatercolorStickerCard(
    badge: BadgeItem,
    onClick: () -> Unit
) {
    val accent = Color(badge.accentHex)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("badge_${badge.id}")
            .clickable(onClick = onClick)
            .alpha(if (badge.isUnlocked) 1f else 0.72f),
        shape = RoundedCornerShape(22.dp),
        color = if (badge.isUnlocked) accent.copy(alpha = 0.14f) else WarmPaper,
        border = BorderStroke(
            width = if (badge.isUnlocked) 2.5.dp else 1.5.dp,
            color = if (badge.isUnlocked) accent else OutlineSand
        ),
        shadowElevation = if (badge.isUnlocked) 5.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = if (badge.isUnlocked) Color.White else Color(0xFFF3EBDD),
                border = BorderStroke(2.dp, if (badge.isUnlocked) accent else OutlineSand),
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (badge.isUnlocked) badge.emoji else "🔒",
                        fontSize = 32.sp
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = badge.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarmInk,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = badge.progressLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { badge.progressRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(50)),
                color = if (badge.isUnlocked) accent else OutlineSand,
                trackColor = Color.White,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
