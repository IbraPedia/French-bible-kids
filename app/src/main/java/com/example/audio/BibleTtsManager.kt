package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class TtsPlaybackState(
    val isReady: Boolean = false,
    val isSpeaking: Boolean = false,
    val activeParagraphIndex: Int = -1,
    val frenchAvailable: Boolean = true
)

class BibleTtsManager(context: Context) {

    private val _state = MutableStateFlow(TtsPlaybackState())
    val state: StateFlow<TtsPlaybackState> = _state.asStateFlow()

    private var tts: TextToSpeech? = null
    private var onParagraphDoneCallback: ((Int) -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val langResult = tts?.setLanguage(Locale.FRENCH)
                val frenchOk = langResult != TextToSpeech.LANG_MISSING_DATA &&
                    langResult != TextToSpeech.LANG_NOT_SUPPORTED
                if (!frenchOk) {
                    tts?.setLanguage(Locale.getDefault())
                }
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        val idx = utteranceId?.removePrefix("p_")?.toIntOrNull() ?: -1
                        _state.value = _state.value.copy(isSpeaking = true, activeParagraphIndex = idx)
                    }

                    override fun onDone(utteranceId: String?) {
                        val idx = utteranceId?.removePrefix("p_")?.toIntOrNull() ?: -1
                        _state.value = _state.value.copy(isSpeaking = false)
                        if (idx >= 0) {
                            onParagraphDoneCallback?.invoke(idx)
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _state.value = _state.value.copy(isSpeaking = false)
                    }
                })
                _state.value = _state.value.copy(isReady = true, frenchAvailable = frenchOk)
            }
        }
    }

    fun speakParagraph(
        text: String,
        paragraphIndex: Int,
        speechRate: Float,
        pitch: Float,
        onDone: (Int) -> Unit
    ) {
        val engine = tts ?: return
        if (!_state.value.isReady) return
        onParagraphDoneCallback = onDone
        engine.setSpeechRate(speechRate.coerceIn(0.6f, 1.4f))
        engine.setPitch(pitch.coerceIn(0.8f, 1.3f))
        val cleanText = stripEmojisForSpeech(text)
        _state.value = _state.value.copy(isSpeaking = true, activeParagraphIndex = paragraphIndex)
        engine.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "p_$paragraphIndex")
    }

    fun speakSample(speechRate: Float, pitch: Float) {
        speakParagraph(
            text = "Bonjour ! Bienvenue dans La Bible App pour les Enfants.",
            paragraphIndex = -1,
            speechRate = speechRate,
            pitch = pitch,
            onDone = {}
        )
    }

    fun stop() {
        onParagraphDoneCallback = null
        tts?.stop()
        _state.value = _state.value.copy(isSpeaking = false)
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }

    private fun stripEmojisForSpeech(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), " ").replace(Regex("\\s+"), " ").trim()
    }
}
