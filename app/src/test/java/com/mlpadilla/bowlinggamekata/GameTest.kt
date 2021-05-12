package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Game
import io.kotest.core.spec.style.BehaviorSpec

class GameTest : BehaviorSpec({
    val game = Game()
    given("a number of pins knocked down") {
        val pinsKnockedDown = 2
        `when`("calling roll") {
            game.roll(pinsKnockedDown)
        }
    }
})