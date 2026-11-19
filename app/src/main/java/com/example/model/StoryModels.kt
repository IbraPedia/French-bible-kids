package com.example.model

enum class Testament(val labelFr: String) {
    ANCIEN_TESTAMENT("Ancien Testament"),
    NOUVEAU_TESTAMENT("Nouveau Testament")
}

enum class ReadingDifficulty(
    val labelFr: String,
    val ageHint: String,
    val defaultTtsRate: Float,
    val descriptionFr: String
) {
    FACILE(
        labelFr = "Facile",
        ageHint = "4–6 ans",
        defaultTtsRate = 0.82f,
        descriptionFr = "Voix douce et lente, texte aéré et indices visuels."
    ),
    MOYEN(
        labelFr = "Moyen",
        ageHint = "7–9 ans",
        defaultTtsRate = 0.96f,
        descriptionFr = "Rythme équilibré avec quiz complet et morale."
    ),
    AVANCE(
        labelFr = "Avancé",
        ageHint = "10–12 ans",
        defaultTtsRate = 1.08f,
        descriptionFr = "Lecture fluide et défi sans indices supplémentaires."
    )
}

enum class SceneBackdrop {
    GARDEN_EDEN,
    ARK_OCEAN,
    DESERT_NIGHT,
    EGYPT_PALACE,
    PRISON_CELL,
    RED_SEA,
    MOUNTAIN_SINAI,
    BATTLEFIELD,
    LION_DEN,
    WHALE_SEA,
    BETHLEHEM_STABLE,
    GALILEE_BOAT,
    VILLAGE_SUNLIT,
    TEMPLE_COURTYARD,
    RESURRECTION_DAWN,
    HEAVEN_CITY
}

enum class CharacterRole {
    NONE,
    GOD_LIGHT,
    ADAM_EVE,
    NOAH,
    ABRAHAM,
    JOSEPH_COAT,
    POTIPHAR_WIFE,
    PRISONER,
    PHARAOH,
    MOSES,
    DAVID,
    GOLIATH,
    DANIEL,
    LION,
    JONAH,
    MARY,
    BABY_JESUS,
    JESUS,
    SHEPHERD,
    FISHERMAN,
    ANGEL,
    PAUL,
    KID_FRIEND
}

data class StoryScene(
    val id: String,
    val title: String,
    val backdrop: SceneBackdrop,
    val primaryChar: CharacterRole,
    val secondaryChar: CharacterRole,
    val moodEmoji: String,
    val propEmoji: String,
    val expression: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class StoryModule(
    val id: Int,
    val title: String,
    val subtitle: String,
    val testament: Testament,
    val emoji: String,
    val badgeTitle: String,
    val badgeEmoji: String,
    val colorHex: Long
)

data class BibleStory(
    val id: Int,
    val moduleId: Int,
    val title: String,
    val reference: String,
    val testament: Testament,
    val emoji: String,
    val durationMinutes: Int,
    val moral: String,
    val paragraphs: List<String>,
    val scenes: List<StoryScene>,
    val questions: List<QuizQuestion>
)
