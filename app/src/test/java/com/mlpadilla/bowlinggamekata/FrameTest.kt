package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import com.mlpadilla.bowlinggamekata.game.Roll
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class FrameTest: BehaviorSpec({

    given("a frame") {
        val frame = Frame()
        then("the player has two rolls") {
            frame.roll1
            frame.roll2
        }
        then("to knock down 10 pins") {
            frame.pins shouldBe 10
        }
    }

    given("a frame with one roll") {
        val frame = Frame(
            roll1 = Roll(2)
        )
        then("first roll is set but second roll isn't") {
            frame.roll1 shouldNotBe null
            frame.roll2 shouldBe null
        }
        `when`("checking pins") {
            val pins = frame.pins
            then("10 - knocked down pins is returned") {
                pins shouldBe 8
            }
        }
    }

    given("a frame with two rolls") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(3)
        )
        then("both rolls should be set") {
            frame.roll1 shouldNotBe null
            frame.roll2 shouldNotBe null
        }
        `when`("checking pins") {
            val pins = frame.pins
            then("10 - knocked down pins is returned") {
                pins shouldBe 5
            }
        }
        `when`("all 10 pins are knocked down") {
            val frame = Frame(
                roll1 = Roll(2),
                roll2 = Roll(8)
            )
            then("this frame is marked as spare") {
                frame.isSpare shouldBe true
            }
        }
        `when`("not all 10 pins are knocked down") {
            then("this frame is not marked as spare") {
                frame.isSpare shouldBe false
            }
        }
    }
//    The score for the frame is the total number of pins knocked down, plus bonuses for strikes and spares
    given("a frame with pins knocked down") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(1),
        )
        `when`("checking score") {
            val pins = frame.score
            then("the total number of pins knocked down is returned") {
                pins shouldBe 3
            }
        }
    }
    given("A spare with information about next roll") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(8),
            nextRoll = Roll(5)
        )
        then("the bonus for 'spare' is next roll's number of pins knocked down") {
            frame.bonus shouldBe 5
            frame.score shouldBe 15
        }
    }
    given("A spare without information about next roll") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(8)
        )

        then("no bonus for 'spare' is added") {
            frame.bonus shouldBe 0
            frame.score shouldBe 10
        }
    }
    given("A frame that is not awarded with 'spare'") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(3)
        )
        frame.isSpare shouldBe false
        then("no bonus for 'spare' is added") {
            frame.bonus shouldBe 0
            frame.score shouldBe 5
        }
        `when`("next roll is provided") {
            val frameWithNextRoll = Frame(
                roll1 = Roll(2),
                roll2 = Roll(3),
                nextRoll = Roll(7)
            )
            frameWithNextRoll.isSpare shouldBe false
            then("no bonus for 'spare' is added") {
                frameWithNextRoll.bonus shouldBe 0
                frameWithNextRoll.score shouldBe 5
            }
        }
    }
})