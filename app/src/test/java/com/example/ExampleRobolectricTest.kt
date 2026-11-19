package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.StoryCatalogLoader
import com.example.data.local.UserPreferencesStore
import com.example.model.SceneBackdrop
import com.example.ui.illustration.watercolorDrawableForScene
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun appNameAndStoryCatalogWithWatercolorScenesAreValid() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("La Bible App pour les Enfants", context.getString(R.string.app_name))

        val stories = StoryCatalogLoader(context).loadAllStories()
        assertEquals(100, stories.size)
        stories.forEach { story ->
            assertTrue("Paragraphs out of range for ${story.id}", story.paragraphs.size in 5..7)
            assertTrue("Scenes out of range for ${story.id}", story.scenes.size in 3..5)
            assertEquals("Expected 5 questions for ${story.id}", 5, story.questions.size)
            story.scenes.forEach { scene ->
                assertNotEquals(0, watercolorDrawableForScene(scene))
            }
        }

        val josephStory = stories.first { it.id == 15 }
        val drawables = josephStory.scenes.map { watercolorDrawableForScene(it) }
        assertTrue(drawables.contains(R.drawable.img_scene_joseph_potiphar))
        assertTrue(drawables.contains(R.drawable.img_scene_joseph_escape))
        assertTrue(drawables.contains(R.drawable.img_scene_prison_cell))
        assertTrue(josephStory.scenes.any { it.backdrop == SceneBackdrop.PRISON_CELL })
    }

    @Test
    fun dailyStreakAwardsXpInDataStore() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val store = UserPreferencesStore(context)
        store.resetAll()

        val firstDay = store.recordCompletionAndStreak(baseXp = 80, storyEmoji = "🌾")
        assertEquals(1, firstDay.newStreak)
        assertEquals(25, firstDay.bonusXpAwarded)
        assertTrue(firstDay.isNewDayStreak)

        val prefs = store.userPreferencesFlow.first()
        assertEquals(1, prefs.currentStreak)
        assertEquals(105, prefs.totalXp)
        assertTrue(prefs.unlockedEmojis.contains("🌾"))
    }
}
