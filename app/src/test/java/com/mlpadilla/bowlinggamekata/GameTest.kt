package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import com.mlpadilla.bowlinggamekata.game.Game
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class GameTest : BehaviorSpec({
    val game = Game()
    given("a number of pins knocked down") {
        val pinsKnockedDown = 2
        `when`("calling roll") {
            game.roll(pinsKnockedDown)
        }
    }

    given("no input") {
        `when`("calling score") {
            val score = game.score()
            then("total score for that game is returned") {
                score shouldBe 0
            }
        }
    }

    given("a game") {
        then("the game consists of 10 frames") {
            game.frames.size shouldBe 10
        }
    }

    //In each frame the player has two rolls to knock down 10 pins
    given("a frame") {
        val frame = Frame()
        then("the player has two rolls") {
            frame.roll1
            frame.roll2
        }
    }
})