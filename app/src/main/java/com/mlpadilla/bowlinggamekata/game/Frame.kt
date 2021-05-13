package com.mlpadilla.bowlinggamekata.game

data class Frame (
    val roll1: Roll? = null,
    val roll2: Roll? = null,
    val nextRoll: Roll? = null
) {
    val score: Int get() =
        safePinsKnockedDownValue(roll1) + safePinsKnockedDownValue(roll2) + bonus
    val pins: Int get() = 10 - score
    val isSpare: Boolean = score == 10
    val bonus: Int = if (isSpare) safePinsKnockedDownValue(nextRoll) else 0

    private fun safePinsKnockedDownValue(roll: Roll?) =
        roll?.pinsKnockedDown ?: 0
}

data class Roll(
    val pinsKnockedDown: Int
)