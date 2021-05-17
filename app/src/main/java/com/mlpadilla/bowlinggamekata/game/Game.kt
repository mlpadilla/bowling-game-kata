package com.mlpadilla.bowlinggamekata.game

class Game {
    private val _frames = MutableList<Frame?>(10) { null }
    val frames: List<Frame?> = _frames

    fun roll(pinsKnockedDown: Int) {
        val latestFrame = _frames.lastOrNull { it != null }
        val newRoll = Roll(pinsKnockedDown)

        _frames.filterNotNull().takeLast(2).forEach { frame ->
            when {
                frame is Frame.FrameWithSpareBonus && frame.nextRoll == null -> {
                    val latestFramePosition = _frames.lastIndexOf(frame)
                    _frames[latestFramePosition] = frame.copy(nextRoll = newRoll)
                }
                frame is Frame.FrameWithStrikeBonus && frame.nextRoll == null -> {
                    val latestFramePosition = _frames.lastIndexOf(frame)
                    _frames[latestFramePosition] = frame.copy(nextRoll = newRoll)
                }
                frame is Frame.FrameWithStrikeBonus && frame.nextSecondRoll == null -> {
                    val latestFramePosition = _frames.lastIndexOf(frame)
                    _frames[latestFramePosition] = frame.copy(nextSecondRoll = newRoll)
                }
            }
        }

        when {
            latestFrame is WithSecondRoll && latestFrame.roll2 == null -> {
                val latestFramePosition = _frames.lastIndexOf(latestFrame)
                _frames[latestFramePosition] = Frame.fromRolls(
                    roll1 = latestFrame.roll1,
                    roll2 = newRoll
                )
            }
            else -> {
                _frames.indexOfFirst { it == null }
                    .takeUnless { it == -1 }
                    ?.let { nextFramePosition ->
                        _frames[nextFramePosition] = Frame.fromRolls(newRoll)
                    }
            }
        }
    }

    fun score(): Int = frames.sumOf { it?.score ?: 0 }
}