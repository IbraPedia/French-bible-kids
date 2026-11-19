package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StoryModule
import com.example.model.Testament
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun ModuleHeaderBanner(
    module: StoryModule,
    completedCount: Int,
    totalCount: Int
) {
    val accent = Color(module.colorHex)
    val bgRes = if (module.testament == Testament.ANCIEN_TESTAMENT) {
        R.drawable.img_world_old_testament
    } else {
        R.drawable.img_world_new_testament
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        color = WarmPaper,
        border = BorderStroke(2.dp, accent.copy(alpha = 0.55f)),
        shadowElevation = 3.dp
    ) {
        Box(modifier = Modifier.height(IntrinsicSize.Min)) {
            Image(
                painter = painterResource(id = bgRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().alpha(0.18f)
            )
            Box(modifier = Modifier.fillMaxSize().background(accent.copy(alpha = 0.14f)))
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(52.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text(module.emoji, fontSize = 28.sp) }
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Module ${module.id} • ${module.testament.labelFr}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent.copy(alpha = 0.95f)
                    )
                    Text(text = module.title, style = MaterialTheme.typography.titleLarge, color = WarmInk)
                    Text(
                        text = module.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha = 0.92f)) {
                    Text(
                        text = "$completedCount/$totalCount ⭐",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmInk,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
