package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.UserPreferences
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.MeadowGreenDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmInk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySheet(
    preferences: UserPreferences,
    onUpdateAccessibility: (
        textScale: Float,
        dyslexiaFriendly: Boolean,
        ttsRate: Float,
        ttsPitch: Float,
        autoAdvance: Boolean
    ) -> Unit,
    onTestFrenchVoice: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ParchmentCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Accessibilité & Lecture 🎧",
                style = MaterialTheme.typography.headlineMedium,
                color = WarmInk
            )
            Spacer(Modifier.height(14.dp))

            // Text size slider
            Text(
                text = "Taille du texte : ${(preferences.textSizeScale * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                color = WarmInk
            )
            Slider(
                value = preferences.textSizeScale,
                onValueChange = {
                    onUpdateAccessibility(
                        it,
                        preferences.dyslexiaFriendly,
                        preferences.ttsSpeechRate,
                        preferences.ttsPitch,
                        preferences.ttsAutoAdvance
                    )
                },
                valueRange = 0.85f..1.45f,
                colors = SliderDefaults.colors(thumbColor = HoneyGold, activeTrackColor = HoneyGold),
                modifier = Modifier.testTag("slider_text_size")
            )

            // Dyslexia-friendly font toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Police adaptée dyslexie (Lexend)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = WarmInk
                    )
                    Text(
                        text = "Lettres bien espacées et lignes aérées pour lire sans fatigue.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = preferences.dyslexiaFriendly,
                    onCheckedChange = {
                        onUpdateAccessibility(
                            preferences.textSizeScale,
                            it,
                            preferences.ttsSpeechRate,
                            preferences.ttsPitch,
                            preferences.ttsAutoAdvance
                        )
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MeadowGreen),
                    modifier = Modifier.testTag("switch_dyslexia_font")
                )
            }

            // TTS speed slider
            Text(
                text = "Vitesse de la voix : ${"%.2fx".format(preferences.ttsSpeechRate)}",
                style = MaterialTheme.typography.titleMedium,
                color = WarmInk
            )
            Slider(
                value = preferences.ttsSpeechRate,
                onValueChange = {
                    onUpdateAccessibility(
                        preferences.textSizeScale,
                        preferences.dyslexiaFriendly,
                        it,
                        preferences.ttsPitch,
                        preferences.ttsAutoAdvance
                    )
                },
                valueRange = 0.65f..1.35f,
                colors = SliderDefaults.colors(thumbColor = SkyBlue, activeTrackColor = SkyBlue),
                modifier = Modifier.testTag("slider_tts_rate")
            )

            // Auto-advance paragraphs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enchaîner les paragraphes automatiquement",
                    style = MaterialTheme.typography.bodyLarge,
                    color = WarmInk,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = preferences.ttsAutoAdvance,
                    onCheckedChange = {
                        onUpdateAccessibility(
                            preferences.textSizeScale,
                            preferences.dyslexiaFriendly,
                            preferences.ttsSpeechRate,
                            preferences.ttsPitch,
                            it
                        )
                    }
                )
            }

            Spacer(Modifier.height(10.dp))
            TactileButton(
                onClick = onTestFrenchVoice,
                containerColor = MeadowGreen,
                shadowColor = MeadowGreenDark,
                contentColor = Color.White,
                testTag = "test_french_tts_button"
            ) {
                Text("Tester la voix française 🔊", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
