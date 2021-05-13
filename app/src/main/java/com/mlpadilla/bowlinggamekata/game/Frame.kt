package com.mlpadilla.bowlinggamekata.game

data class Frame (
    val roll1: Roll? = null,
    val roll2: Roll? = null,
    val nextRoll: Roll? = null
) {
    val score: Int get() =
        (roll1?.pinsKnockedDown ?: 0) + (roll2?.pinsKnockedDown ?: 0) + bonus
    val pins: Int get() = 10 - score
    val isSpare: Boolean = score == 10
    val bonus: Int = if (isSpare) nextRoll?.pinsKnockedDown ?: 0 else 0
}

data class Roll(
    val pinsKnockedDown: Int
)