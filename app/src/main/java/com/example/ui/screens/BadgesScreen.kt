package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BadgeItem
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk

@Composable
fun BadgesScreen(
    badges: List<BadgeItem>,
    modifier: Modifier = Modifier
) {
    var inspectedBadge by remember { mutableStateOf<BadgeItem?>(null) }
    val unlockedCount = badges.count { it.isUnlocked }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 152.dp),
        modifier = modifier
            .fillMaxSize()
            .background(ParchmentCream),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, OutlineSand),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_badges_banner),
                        contentDescription = "Étagère aquarelle des trésors",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)))
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Mes Autocollants Aquarelle 🎨",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = "$unlockedCount / ${badges.size} autocollants débloqués dans ton album",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.92f)
                        )
                    }
                }
            }
        }

        items(badges, key = { it.id }) { badge ->
            WatercolorStickerCard(
                badge = badge,
                onClick = { inspectedBadge = badge }
            )
        }
    }

    inspectedBadge?.let { badge ->
        AlertDialog(
            onDismissRequest = { inspectedBadge = null },
            containerColor = ParchmentCream,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(badge.emoji, fontSize = 32.sp)
                    Text(badge.title, style = MaterialTheme.typography.titleLarge, color = WarmInk)
                }
            },
            text = {
                Column {
                    Text(badge.categoryLabel, style = MaterialTheme.typography.labelSmall, color = Color(badge.accentHex))
                    Spacer(Modifier.height(6.dp))
                    Text(badge.description, style = MaterialTheme.typography.bodyLarge, color = WarmInk)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = if (badge.isUnlocked) "✅ Autocollant débloqué et collé dans ton album !" else "Progression : ${badge.progressLabel}",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (badge.isUnlocked) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { inspectedBadge = null }) { Text("Fermer ✨") }
            }
        )
    }
}
