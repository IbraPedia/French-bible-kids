package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.TactileButton
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    childName: String,
    storiesLoaded: Boolean,
    onContinue: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val transition = rememberInfiniteTransition(label = "splash_anim")
    val floatY by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float_y"
    )
    val pulseScale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "pulse"
    )

    LaunchedEffect(Unit) { visible = true }
    LaunchedEffect(storiesLoaded) {
        if (storiesLoaded) {
            delay(2500)
            onContinue()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_storybook),
            contentDescription = "Illustration aquarelle de bienvenue",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(ParchmentCream.copy(alpha = 0.45f), Color.Transparent, ParchmentCream.copy(alpha = 0.88f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("✨", fontSize = 26.sp, modifier = Modifier.offset(y = floatY.dp))
                Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.85f)) {
                    Text(
                        text = "✅ 100% Hors Ligne",
                        style = MaterialTheme.typography.labelSmall,
                        color = MeadowGreenDark,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Text("🕊️", fontSize = 26.sp, modifier = Modifier.offset(y = (-floatY).dp))
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(700)) + scaleIn(spring(dampingRatio = 0.65f))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f),
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(92.dp).scale(pulseScale)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("📖", fontSize = 46.sp) }
                    }
                    Spacer(Modifier.height(18.dp))
                    Text(
                        text = "La Bible App\npour les Enfants",
                        style = MaterialTheme.typography.displayLarge,
                        color = WarmInk,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.85f)) {
                        Text(
                            text = "Bonjour $childName ! 🌈\n100 histoires illustrées à découvrir",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = WarmInk,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.widthIn(max = 420.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                TactileButton(
                    onClick = onContinue,
                    enabled = storiesLoaded,
                    containerColor = MeadowGreen,
                    shadowColor = MeadowGreenDark,
                    contentColor = Color.White,
                    testTag = "splash_start_button"
                ) {
                    Text(
                        text = if (storiesLoaded) "Ouvrir mon Livre ✨" else "Préparation des histoires...",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Lecture audio en français • Quiz & Badges",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmInk,
                    modifier = Modifier.alpha(0.8f)
                )
            }
        }
    }
}
