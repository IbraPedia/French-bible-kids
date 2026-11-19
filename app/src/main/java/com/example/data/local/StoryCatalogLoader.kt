package com.example.data.local

import android.content.Context
import com.example.model.BibleStory
import com.example.model.CharacterRole
import com.example.model.QuizQuestion
import com.example.model.SceneBackdrop
import com.example.model.StoryScene
import com.example.model.Testament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class StoryCatalogLoader(private val context: Context) {

    @Volatile
    private var cachedStories: List<BibleStory>? = null

    suspend fun loadAllStories(): List<BibleStory> = withContext(Dispatchers.IO) {
        cachedStories?.let { return@withContext it }

        val loaded = mutableListOf<BibleStory>()
        for (chunk in 1..20) {
            val fileName = "stories_${chunk.toString().padStart(2, '0')}.json"
            val jsonText = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val array = JSONArray(jsonText)
            for (i in 0 until array.length()) {
                loaded.add(parseStory(array.getJSONObject(i)))
            }
        }
        val sorted = loaded.sortedBy { it.id }
        cachedStories = sorted
        sorted
    }

    private fun parseStory(obj: JSONObject): BibleStory {
        val testament = if (obj.getString("testament") == "NOUVEAU") {
            Testament.NOUVEAU_TESTAMENT
        } else {
            Testament.ANCIEN_TESTAMENT
        }

        val paragraphsJson = obj.getJSONArray("paragraphs")
        val paragraphs = List(paragraphsJson.length()) { paragraphsJson.getString(it) }

        val scenesJson = obj.getJSONArray("scenes")
        val scenes = List(scenesJson.length()) { idx ->
            val s = scenesJson.getJSONObject(idx)
            StoryScene(
                id = s.getString("id"),
                title = s.getString("title"),
                backdrop = enumValueOrDefault(s.getString("backdrop"), SceneBackdrop.GARDEN_EDEN),
                primaryChar = enumValueOrDefault(s.getString("primaryChar"), CharacterRole.KID_FRIEND),
                secondaryChar = enumValueOrDefault(s.optString("secondaryChar", "NONE"), CharacterRole.NONE),
                moodEmoji = s.optString("moodEmoji", "✨"),
                propEmoji = s.optString("propEmoji", "📖"),
                expression = s.optString("expression", "HAPPY")
            )
        }

        val questionsJson = obj.getJSONArray("questions")
        val questions = List(questionsJson.length()) { idx ->
            val q = questionsJson.getJSONObject(idx)
            val optsJson = q.getJSONArray("options")
            QuizQuestion(
                question = q.getString("question"),
                options = List(optsJson.length()) { optsJson.getString(it) },
                correctIndex = q.getInt("correctIndex"),
                explanation = q.getString("explanation")
            )
        }

        return BibleStory(
            id = obj.getInt("id"),
            moduleId = obj.getInt("moduleId"),
            title = obj.getString("title"),
            reference = obj.getString("reference"),
            testament = testament,
            emoji = obj.getString("emoji"),
            durationMinutes = obj.optInt("durationMinutes", 3),
            moral = obj.getString("moral"),
            paragraphs = paragraphs,
            scenes = scenes,
            questions = questions
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(name: String, default: T): T {
        return enumValues<T>().firstOrNull { it.name == name } ?: default
    }
}
