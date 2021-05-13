package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FrameTest: BehaviorSpec({
    //In each frame the player has two rolls to knock down 10 pins
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
})