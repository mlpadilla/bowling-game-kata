package com.mlpadilla.bowlinggamekata.game

sealed interface Frame {
    companion object {
        const val TOTAL_NUMBER_OF_PINS = 10
        private const val MAXIMUM_SCORE = TOTAL_NUMBER_OF_PINS
        fun fromRolls(
            roll1: Roll? = null,
            roll2: Roll? = null,
            nextRoll: Roll? = null,
            nextSecondRoll: Roll? = null
        ): Frame {
            if (roll1.toScore() == MAXIMUM_SCORE) {
                return FrameWithStrikeBonus(
                    roll1 = roll1,
                    nextRoll,
                    nextSecondRoll
                )
            }
            if (roll1.toScore() + roll2.toScore() == MAXIMUM_SCORE) {
                return FrameWithSpareBonus(
                    roll1 = roll1,
                    roll2 = roll2,
                    nextRoll = nextRoll
                )
            }
            return FrameWithNoBonus(roll1, roll2)
        }
    }
    val roll1: Roll?
    val score: Int
    val pinsUp: Int get() = TOTAL_NUMBER_OF_PINS - score

    data class FrameWithNoBonus(
        override val roll1: Roll?,
        override val roll2: Roll?,
    ) : Frame, WithSecondRoll {
        override val score: Int = roll1.toScore() + roll2.toScore()
    }

    data class FrameWithSpareBonus(
        override val roll1: Roll?,
        override val roll2: Roll?,
        override val nextRoll: Roll?,
    ) : Frame, WithBonus, WithSecondRoll, WithNextRoll {
        override val bonus: Int = nextRoll.toScore()
        override val score: Int = roll1.toScore() + roll2.toScore() + bonus
    }

    data class FrameWithStrikeBonus(
        override val roll1: Roll?,
        override val nextRoll: Roll?,
        override val nextSecondRoll: Roll?,
    ) : Frame, WithBonus, WithNextRoll, WithNextSecondRoll {
        override val bonus: Int = nextRoll.toScore() + nextSecondRoll.toScore()
        override val score: Int = roll1.toScore() + bonus
    }
}

interface WithSecondRoll {
    val roll2: Roll?
}

interface WithNextRoll {
    val nextRoll: Roll?
}

interface WithNextSecondRoll {
    val nextSecondRoll: Roll?
}

interface WithBonus {
    val bonus: Int
}

private fun Roll?.toScore() = this?.pinsKnockedDown ?: 0