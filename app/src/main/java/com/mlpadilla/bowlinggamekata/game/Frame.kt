package com.mlpadilla.bowlinggamekata.game

class Frame (
    val roll1: Roll? = null,
    val roll2: Roll? = null,
    val score: Int = (roll1?.pinsKnockedDown ?: 0) + (roll2?.pinsKnockedDown ?: 0),
    val pins: Int = 10 - score
)

data class Roll(
    val pinsKnockedDown: Int
)