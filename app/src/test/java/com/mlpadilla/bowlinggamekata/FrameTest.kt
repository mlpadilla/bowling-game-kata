package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import com.mlpadilla.bowlinggamekata.game.Roll
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldNotBeInstanceOf

class FrameTest: BehaviorSpec({

    given("a frame") {
        val frame = Frame.fromRolls() as Frame.FrameWithNoBonus
        then("the player has two rolls") {
            frame.roll1
            frame.roll2
        }
        then("to knock down 10 pins") {
            frame.pinsUp shouldBe 10
        }
    }

    given("a frame with one roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2)
        ) as Frame.FrameWithNoBonus
        then("first roll is set but second roll isn't") {
            frame.roll1 shouldNotBe null
            frame.roll2 shouldBe null
        }
        `when`("checking pins") {
            val pins = frame.pinsUp
            then("10 - knocked down pins is returned") {
                pins shouldBe 8
            }
        }
    }

    given("a frame with two rolls") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(3)
        ) as Frame.FrameWithNoBonus
        then("both rolls should be set") {
            frame.roll1 shouldNotBe null
            frame.roll2 shouldNotBe null
        }
        `when`("checking pins") {
            val pins = frame.pinsUp
            then("10 - knocked down pins is returned") {
                pins shouldBe 5
            }
        }
        `when`("all 10 pins are knocked down") {
            val frame = Frame.fromRolls(
                roll1 = Roll(2),
                roll2 = Roll(8)
            )
            then("this frame is marked as spare") {
                frame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
            }
        }
        `when`("not all 10 pins are knocked down") {
            then("this frame is not marked as spare") {
                frame.shouldNotBeInstanceOf<Frame.FrameWithSpareBonus>()
            }
        }
    }
//    The score for the frame is the total number of pins knocked down, plus bonuses for strikes and spares
    given("a frame with pins knocked down") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(1),
        ) as Frame.FrameWithNoBonus
        `when`("checking score") {
            val pins = frame.score
            then("the total number of pins knocked down is returned") {
                pins shouldBe 3
            }
        }
    }
    given("A spare with information about next roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(8),
            nextRoll = Roll(5)
        ) as Frame.FrameWithSpareBonus
        then("the bonus for 'spare' is next roll's number of pins knocked down") {
            frame.bonus shouldBe 5
            frame.score shouldBe 15
        }
    }
    given("A spare without information about next roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(8)
        ) as Frame.FrameWithSpareBonus

        then("no bonus for 'spare' is added") {
            frame.bonus shouldBe 0
            frame.score shouldBe 10
        }
    }
    given("A frame that is not awarded with 'spare'") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(3)
        )
        frame.shouldNotBeInstanceOf<Frame.FrameWithSpareBonus>()
        then("no bonus for 'spare' is added") {
            frame.score shouldBe 5
        }
        `when`("next roll is provided") {
            val frameWithNextRoll = Frame.fromRolls(
                roll1 = Roll(2),
                roll2 = Roll(3),
                nextRoll = Roll(7)
            )
            frameWithNextRoll.shouldNotBeInstanceOf<Frame.FrameWithSpareBonus>()
            then("no bonus for 'spare' is added") {
                frameWithNextRoll.score shouldBe 5
            }
        }
    }

//    A strike is when the player knocks down all 10 pins on his first roll
    given("A frame with one roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(pinsKnockedDown = 10)
        )
        `when`("all 10 pins are knocked down") {
            then("this frame is marked as strike") {
                frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
            }
        }
        `when`("it is marked as strike") {
            frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
            then("it is not marked as spare") {
                frame.shouldNotBeInstanceOf<Frame.FrameWithSpareBonus>()
            }
        }
    }
})