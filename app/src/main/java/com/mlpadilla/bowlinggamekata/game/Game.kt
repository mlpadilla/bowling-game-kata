package com.mlpadilla.bowlinggamekata.game

class Game {
    val frames = List<Frame>(10) { object : Frame {} }

    fun roll(pinsKnockedDown: Int) {}

    fun score(): Int = 0
}