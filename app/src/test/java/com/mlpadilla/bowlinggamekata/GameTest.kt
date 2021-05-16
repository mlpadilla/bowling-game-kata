package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import com.mlpadilla.bowlinggamekata.game.Game
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class GameTest : BehaviorSpec({

    given("a number of pins knocked down") {
        val game = Game()
        val pinsKnockedDown = 2
        `when`("calling roll") {
            game.roll(pinsKnockedDown)
        }
    }

    given("no input") {
        val game = Game()
        `when`("calling score") {
            val score = game.score()
            then("total score for that game is returned") {
                score shouldBe 0
            }
        }
    }

    given("a game") {
        val game = Game()
        then("the game consists of 10 frames") {
            game.frames.size shouldBe 10
        }
        then("no frame holds any data") {
            game.frames.forEach {
                it shouldBe null
            }
        }
        then("the initial score is zero") {
            game.score() shouldBe 0
        }
        `when`("rolling the first ball") {
            game.roll(pinsKnockedDown = 2)
            then("the first frame contains the value of the first roll") {
                game.frames.first()!!.let { firstFrame ->
                    firstFrame.roll1!!.pinsKnockedDown shouldBe 2
                    firstFrame.score shouldBe 2
                }
            }
            then("the score of the game is the first roll's score") {
                game.score() shouldBe 2
            }
        }
    }

    // In the tenth frame a player who rolls a spare or strike is allowed to roll the extra balls to complete the frame.
    //TODO Update Game tests for roll/frame update sequence considering newly defined rules.
    // Then enable these tests.
    xgiven("a game on the tenth frame") {
        `xwhen`("a player who rolls spare") {
            val game = Game()
            for(i in 0..8) {
                game.roll(pinsKnockedDown = 10)
            }
            game.roll(pinsKnockedDown = 2)
            game.roll(pinsKnockedDown = 8)
            game.frames[9].let { tenthFrame ->
                tenthFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
                tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                tenthFrame.roll2!!.pinsKnockedDown shouldBe 8
                tenthFrame.nextRoll shouldBe null
            }
            xthen("is allowed to roll the extra ball to complete the frame") {
                game.roll(3)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                    tenthFrame.roll2!!.pinsKnockedDown shouldBe 8
                    tenthFrame.nextRoll!!.pinsKnockedDown shouldBe 3
                }
            }
        }
        `xwhen`("a player who rolls strike") {
            val game = Game()
            for(i in 0..8) {
                game.roll(pinsKnockedDown = 10)
            }
            game.roll(pinsKnockedDown = 10)
            game.frames[9].let { tenthFrame ->
                tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                tenthFrame.nextRoll shouldBe null
                tenthFrame.nextSecondRoll shouldBe null
            }
            then("is allowed to roll the extra balls to complete the frame") {
                game.roll(3)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                    tenthFrame.nextRoll shouldBe 3
                    tenthFrame.nextSecondRoll shouldBe null
                }
                game.roll(4)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                    tenthFrame.nextRoll shouldBe 3
                    tenthFrame.nextSecondRoll shouldBe 4
                }
            }
        }
    }
})