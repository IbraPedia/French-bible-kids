package com.example.ui.illustration

import androidx.annotation.DrawableRes
import com.example.R
import com.example.model.CharacterRole
import com.example.model.SceneBackdrop
import com.example.model.StoryScene

@DrawableRes
fun watercolorDrawableForScene(scene: StoryScene): Int {
    return when (scene.backdrop) {
        SceneBackdrop.GARDEN_EDEN -> {
            if (scene.primaryChar == CharacterRole.ADAM_EVE || scene.secondaryChar == CharacterRole.ADAM_EVE) {
                R.drawable.img_scene_adam_eve
            } else {
                R.drawable.img_scene_garden_eden
            }
        }
        SceneBackdrop.ARK_OCEAN -> R.drawable.img_scene_ark_ocean
        SceneBackdrop.DESERT_NIGHT -> R.drawable.img_scene_desert_night
        SceneBackdrop.EGYPT_PALACE -> when {
            scene.expression == "TEMPTATION" ||
                (scene.primaryChar == CharacterRole.POTIPHAR_WIFE && scene.secondaryChar == CharacterRole.JOSEPH_COAT) ->
                R.drawable.img_scene_joseph_potiphar
            scene.expression == "BRAVE" &&
                (scene.primaryChar == CharacterRole.JOSEPH_COAT || scene.secondaryChar == CharacterRole.POTIPHAR_WIFE) ->
                R.drawable.img_scene_joseph_escape
            else -> R.drawable.img_scene_egypt_palace
        }
        SceneBackdrop.PRISON_CELL -> {
            if (scene.secondaryChar == CharacterRole.GOD_LIGHT ||
                scene.secondaryChar == CharacterRole.ANGEL ||
                scene.expression == "PEACEFUL"
            ) {
                R.drawable.img_scene_prison_light
            } else {
                R.drawable.img_scene_prison_cell
            }
        }
        SceneBackdrop.RED_SEA -> R.drawable.img_scene_red_sea
        SceneBackdrop.MOUNTAIN_SINAI -> R.drawable.img_scene_mountain_sinai
        SceneBackdrop.BATTLEFIELD -> R.drawable.img_scene_battlefield
        SceneBackdrop.LION_DEN -> R.drawable.img_scene_lion_den
        SceneBackdrop.WHALE_SEA -> R.drawable.img_scene_whale_sea
        SceneBackdrop.BETHLEHEM_STABLE -> R.drawable.img_scene_bethlehem_stable
        SceneBackdrop.GALILEE_BOAT -> R.drawable.img_scene_galilee_boat
        SceneBackdrop.VILLAGE_SUNLIT -> {
            if (scene.primaryChar == CharacterRole.JESUS || scene.secondaryChar == CharacterRole.JESUS) {
                R.drawable.img_scene_jesus_children
            } else {
                R.drawable.img_scene_village_sunlit
            }
        }
        SceneBackdrop.TEMPLE_COURTYARD -> R.drawable.img_scene_temple_courtyard
        SceneBackdrop.RESURRECTION_DAWN -> R.drawable.img_scene_resurrection_dawn
        SceneBackdrop.HEAVEN_CITY -> R.drawable.img_scene_heaven_city
    }
}
