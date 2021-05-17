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

    given("a game (that will register a no-bonus frame)") {
        val game = Game()
        `when`("registering two rolls knocking down 2 and 3 pins") {
            game.roll(2)
            game.roll(3)
            val firstFrame = game.frames.first()!!
            then("frame has no bonus") {
                firstFrame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
            }
            then("both rolls are registered for the same frame") {
                game.frames.first()!!.let { firstFrame ->
                    require(firstFrame is Frame.FrameWithNoBonus)
                    firstFrame.roll1!!.pinsKnockedDown shouldBe 2
                    firstFrame.roll2!!.pinsKnockedDown shouldBe 3
                    firstFrame.score shouldBe 5
                }
                game.frames.filterNotNull().size shouldBe 1
            }
            then("rolls are registered in the game score") {
                game.score() shouldBe 5
            }
        }
    }

    given("a game (that will register a spare)") {
        val game = Game()
        `when`("registering two rolls knocking down 2 and 8 pins") {
            game.roll(2)
            game.roll(8)
            val firstFrame = game.frames.first()!!
            then("frame is a spare") {
                firstFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
            }
            then("both rolls are registered for the same frame") {
                game.frames.first()!!.let { firstFrame ->
                    require(firstFrame is Frame.FrameWithSpareBonus)
                    firstFrame.roll1!!.pinsKnockedDown shouldBe 2
                    firstFrame.roll2!!.pinsKnockedDown shouldBe 8
                    firstFrame.score shouldBe 10
                }
                game.frames.filterNotNull().size shouldBe 1
            }
            then("rolls are registered in the game score") {
                game.score() shouldBe 10
            }
        }
        `when`("registering another roll of 3 points") {
            game.roll(pinsKnockedDown = 3)
            then("a new frame registers the roll") {
                game.frames[1]!!.let { secondFrame ->
                    secondFrame.roll1!!.pinsKnockedDown shouldBe 3
                    secondFrame.score shouldBe 3
                }
            }
            then("the spare frame registers a 3-points bonus") {
                game.frames.first()!!.let { firstFrame ->
                    firstFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
                    firstFrame.roll1!!.pinsKnockedDown shouldBe 2
                    firstFrame.roll2!!.pinsKnockedDown shouldBe 8
                    firstFrame.nextRoll!!.pinsKnockedDown shouldBe 3
                    firstFrame.score shouldBe 13
                }
            }
            then("the game score should be (2+8+3)+3=16") {
                game.score() shouldBe 16
            }
        }
    }

    given("a game (that will register a strike)") {
        val game = Game()
        `when`("registering two rolls knocking down 10 and 8 pins") {
            game.roll(10)
            game.roll(8)
            val firstFrame = game.frames.first()!!
            then("the first frame is a strike") {
                firstFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                firstFrame.roll1!!.pinsKnockedDown shouldBe 10
                firstFrame.score shouldBe 18
            }
            then("the second frame has a first roll of 8 points") {
                game.frames[1]!!.let { secondFrame ->
                    secondFrame.roll1!!.pinsKnockedDown shouldBe 8
                    secondFrame.score shouldBe 8
                }
            }
            then("the strike registers an 8-points bonus") {
                firstFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                firstFrame.nextRoll!!.pinsKnockedDown shouldBe 8
                firstFrame.bonus shouldBe 8
            }
            then("the game registers two frames in total") {
                game.frames.filterNotNull().size shouldBe 2
            }
            then("rolls are registered in the game score: (10+8)+8=26") {
                game.score() shouldBe 26
            }
        }
    }

    given("a game (that will register two strikes in a row)") {
        val game = Game()
        `when`("registering three rolls knocking down 10, 10 and 2 pins each") {
            game.roll(pinsKnockedDown = 10)
            game.roll(pinsKnockedDown = 10)
            game.roll(pinsKnockedDown = 2)
            then("the first frame is a strike and has a 10-points and 8-points bonus") {
                game.frames.first()!!.let { firstFrame ->
                    firstFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    firstFrame.roll1!!.pinsKnockedDown shouldBe 10
                    firstFrame.nextRoll!!.pinsKnockedDown shouldBe 10
                    firstFrame.nextSecondRoll!!.pinsKnockedDown shouldBe 2
                    firstFrame.bonus shouldBe 12
                    firstFrame.score shouldBe 22
                }
            }
            then("the second frame is a strike and has an 8-points bonus") {
                game.frames[1]!!.let { secondFrame ->
                    secondFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    secondFrame.roll1!!.pinsKnockedDown shouldBe 10
                    secondFrame.nextRoll!!.pinsKnockedDown shouldBe 2
                    secondFrame.nextSecondRoll shouldBe null
                    secondFrame.bonus shouldBe 2
                    secondFrame.score shouldBe 12
                }
            }
            then("the third frame has no bonus and only a first roll of 8 points") {
                game.frames[2]!!.let { thirdFrame ->
                    thirdFrame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
                    thirdFrame.roll1!!.pinsKnockedDown shouldBe 2
                    thirdFrame.roll2 shouldBe null
                    thirdFrame.score shouldBe 2
                }
            }
            then("rolls are registered in the game score: (10+10+2)+(10+2)+2=36") {
                game.score() shouldBe 36
            }
        }
    }

    given("a game on the tenth frame") {
        `when`("a player who rolls spare") {
            val game = Game()
            for(i in 0..17) {
                game.roll(pinsKnockedDown = 4)
            }
            game.roll(pinsKnockedDown = 2)
            game.roll(pinsKnockedDown = 8)
            game.frames[9].let { tenthFrame ->
                tenthFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
                tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                tenthFrame.roll2!!.pinsKnockedDown shouldBe 8
                tenthFrame.nextRoll shouldBe null
            }
            then("is allowed to roll the extra ball to complete the frame") {
                game.roll(3)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 2
                    tenthFrame.roll2!!.pinsKnockedDown shouldBe 8
                    tenthFrame.nextRoll!!.pinsKnockedDown shouldBe 3
                }
            }
            then("the tenth frame's score should be 2+8+3=13") {
                game.frames[9]!!.score shouldBe 13
            }
            then("the game score should be 72+13=85") {
                game.score() shouldBe 85
            }
        }
        `when`("a player who rolls strike") {
            val game = Game()
            for(i in 0..17) {
                game.roll(pinsKnockedDown = 4)
            }
            game.roll(pinsKnockedDown = 10)
            game.frames[9].let { tenthFrame ->
                tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                tenthFrame.roll1!!.pinsKnockedDown shouldBe 10
                tenthFrame.nextRoll shouldBe null
                tenthFrame.nextSecondRoll shouldBe null
            }
            then("is allowed to roll the extra balls to complete the frame") {
                game.roll(3)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 10
                    tenthFrame.nextRoll!!.pinsKnockedDown shouldBe 3
                    tenthFrame.nextSecondRoll shouldBe null
                }
                game.roll(4)
                game.frames[9].let { tenthFrame ->
                    tenthFrame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
                    tenthFrame.roll1!!.pinsKnockedDown shouldBe 10
                    tenthFrame.nextRoll!!.pinsKnockedDown shouldBe 3
                    tenthFrame.nextSecondRoll!!.pinsKnockedDown shouldBe 4
                }
            }
            then("the tenth frame's score should be 10+3+4=17") {
                game.frames[9]!!.score shouldBe 17
            }
            then("the game score should be 72+17=89") {
                game.score() shouldBe 89
            }
        }
    }
})