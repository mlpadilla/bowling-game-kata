package com.mlpadilla.bowlinggamekata.game

class Game {
    val frames = List<Frame?>(10) { null }

    fun roll(pinsKnockedDown: Int) {}

    fun score(): Int = 0
}