package com.mlpadilla.bowlinggamekata.game

class Game {
    private val _frames = MutableList<Frame?>(10) { null }
    val frames: List<Frame?> = _frames

    fun roll(pinsKnockedDown: Int) {
        _frames.indexOfFirst { it == null }
            .takeUnless { it == -1 }
            ?.let { nextFramePosition ->
                _frames[nextFramePosition] = Frame.fromRolls(Roll(pinsKnockedDown))
            }
    }

    fun score(): Int = frames.sumOf { it?.score ?: 0 }
}