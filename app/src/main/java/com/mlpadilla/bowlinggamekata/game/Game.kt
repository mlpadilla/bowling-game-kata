package com.mlpadilla.bowlinggamekata.game

class Game {
    val frames = List(10) { Frame.fromRolls() }

    fun roll(pinsKnockedDown: Int) {}

    fun score(): Int = 0
}