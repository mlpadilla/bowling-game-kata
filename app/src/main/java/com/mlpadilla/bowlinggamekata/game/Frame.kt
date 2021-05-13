package com.mlpadilla.bowlinggamekata.game

data class Frame (
    val roll1: Roll? = null,
    val roll2: Roll? = null,

) {
    val score: Int get() = (roll1?.pinsKnockedDown ?: 0) + (roll2?.pinsKnockedDown ?: 0)
    val pins: Int get() = 10 - score
}

data class Roll(
    val pinsKnockedDown: Int
)