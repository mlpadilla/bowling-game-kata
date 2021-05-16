package com.mlpadilla.bowlinggamekata.game

class Game {
    private val _frames = MutableList<Frame?>(10) { null }
    val frames: List<Frame?> = _frames

    fun roll(pinsKnockedDown: Int) {
        val latestFrame = _frames.lastOrNull { it != null }

        when {
            latestFrame is WithSecondRoll && latestFrame.roll2 == null -> {
                val latestFramePosition = _frames.lastIndexOf(latestFrame)
                _frames[latestFramePosition] = Frame.fromRolls(
                    roll1 = latestFrame.roll1,
                    roll2 = Roll(pinsKnockedDown)
                )
            }
            else -> {
                _frames.indexOfFirst { it == null }
                    .takeUnless { it == -1 }
                    ?.let { nextFramePosition ->
                        _frames[nextFramePosition] = Frame.fromRolls(Roll(pinsKnockedDown))
                    }
            }
        }
    }

    fun score(): Int = frames.sumOf { it?.score ?: 0 }
}