package com.example.ui.illustration

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryScene
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun WatercolorSceneCard(
    scene: StoryScene,
    sceneNumber: Int,
    totalScenes: Int,
    parallaxOffsetPx: Float = 0f,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "watercolor_ken_burns_${scene.id}")
    val kenBurnsScale by transition.animateFloat(
        initialValue = 1.04f,
        targetValue = 1.11f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 6500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "kb_scale"
    )
    val kenBurnsPanX by transition.animateFloat(
        initialValue = if (sceneNumber % 2 == 0) 10f else -10f,
        targetValue = if (sceneNumber % 2 == 0) -10f else 10f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 7200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "kb_pan_x"
    )
    val glowAlpha by transition.animateFloat(
        initialValue = 0.04f,
        targetValue = 0.14f,
        animationSpec = infiniteRepeatable(tween(3400), RepeatMode.Reverse),
        label = "warm_glow"
    )
    val artworkRes = watercolorDrawableForScene(scene)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = WarmPaper,
        border = BorderStroke(2.dp, OutlineSand),
        shadowElevation = 5.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Image(
                    painter = painterResource(id = artworkRes),
                    contentDescription = scene.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = kenBurnsScale
                            scaleY = kenBurnsScale
                            translationX = kenBurnsPanX
                            translationY = parallaxOffsetPx
                        }
                )

                // Soft warm picture-book sunlight wash & edge blend
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFF8E1).copy(alpha = glowAlpha),
                                    Color.Transparent,
                                    WarmPaper.copy(alpha = 0.24f)
                                )
                            )
                        )
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = WarmPaper.copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, OutlineSand)
                ) {
                    Text(
                        text = "Scène $sceneNumber/$totalScenes",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmInk,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(WarmPaper.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = scene.moodEmoji, fontSize = 18.sp)
                    Text(text = scene.propEmoji, fontSize = 18.sp)
                }
            }

            Text(
                text = scene.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarmInk,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}
