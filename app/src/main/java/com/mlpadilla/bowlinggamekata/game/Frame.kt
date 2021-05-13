package com.mlpadilla.bowlinggamekata.game

class Frame (
    val roll1: Roll? = null,
    val roll2: Roll? = null,
    val pins: Int = 10 - (roll1?.pinsKnockedDown ?: 0) - (roll2?.pinsKnockedDown ?:0)
)

data class Roll(
    val pinsKnockedDown: Int
)