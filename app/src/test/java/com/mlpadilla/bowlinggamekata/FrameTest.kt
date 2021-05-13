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
    }
//    The score for the frame is the total number of pins knocked down, plus bonuses for strikes and spares
    given("a frame with pins knocked down") {
        val frame = Frame(
            roll1 = Roll(2),
            roll2 = Roll(1)
        )
        `when`("checking score") {
            val pins = frame.score
            then("the total number of pins knocked down is returned") {
                pins shouldBe 3
            }
        }
    }
})