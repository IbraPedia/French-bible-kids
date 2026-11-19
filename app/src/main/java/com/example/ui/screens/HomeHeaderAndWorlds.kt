package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Testament
import com.example.ui.components.StorybookPill
import com.example.ui.components.TactileButton
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.viewmodel.MainUiState

@Composable
fun HomeTopHeader(
    uiState: MainUiState,
    onOpenStory: (Int) -> Unit
) {
    val prefs = uiState.preferences
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = HoneyGold.copy(alpha = 0.25f),
                    border = BorderStroke(2.dp, HoneyGold),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) { Text(prefs.avatarEmoji, fontSize = 24.sp) }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Bonjour ${prefs.childName} !",
                        style = MaterialTheme.typography.titleMedium,
                        color = WarmInk
                    )
                    Text(
                        text = "${uiState.completedStoriesCount}/100 histoires • Hors ligne ✅",
                        style = MaterialTheme.typography.labelSmall,
                        color = MeadowGreenDark
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StorybookPill(emoji = "🔥", text = "${prefs.currentStreak}j")
                StorybookPill(emoji = "⭐", text = "${prefs.totalXp}")
            }
        }
        Spacer(Modifier.height(14.dp))
        TactileButton(
            onClick = { onOpenStory(uiState.nextSuggestedStoryId) },
            containerColor = MeadowGreen,
            shadowColor = MeadowGreenDark,
            contentColor = Color.White,
            testTag = "continue_reading_button"
        ) {
            Text(
                text = "Continuer l'histoire n°${uiState.nextSuggestedStoryId} 📖",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun WorldCardsCarousel(
    selectedTestament: Testament?,
    onSelectTestament: (Testament?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            WorldIllustrationCard(
                title = "Ancien Testament",
                subtitle = "50 histoires • Création à Jonas",
                drawableRes = R.drawable.img_world_old_testament,
                selected = selectedTestament == Testament.ANCIEN_TESTAMENT,
                onClick = {
                    onSelectTestament(
                        if (selectedTestament == Testament.ANCIEN_TESTAMENT) null else Testament.ANCIEN_TESTAMENT
                    )
                }
            )
        }
        item {
            WorldIllustrationCard(
                title = "Nouveau Testament",
                subtitle = "50 histoires • Jésus & l'Église",
                drawableRes = R.drawable.img_world_new_testament,
                selected = selectedTestament == Testament.NOUVEAU_TESTAMENT,
                onClick = {
                    onSelectTestament(
                        if (selectedTestament == Testament.NOUVEAU_TESTAMENT) null else Testament.NOUVEAU_TESTAMENT
                    )
                }
            )
        }
    }
}

@Composable
private fun WorldIllustrationCard(
    title: String,
    subtitle: String,
    drawableRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(260.dp)
            .height(145.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(if (selected) 3.dp else 1.5.dp, if (selected) HoneyGold else OutlineSand),
        shadowElevation = if (selected) 6.dp else 2.dp
    ) {
        Box {
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.62f))))
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}
