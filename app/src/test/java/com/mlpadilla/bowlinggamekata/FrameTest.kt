package com.mlpadilla.bowlinggamekata

import com.mlpadilla.bowlinggamekata.game.Frame
import com.mlpadilla.bowlinggamekata.game.Roll
import com.mlpadilla.bowlinggamekata.game.WithSecondRoll
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldNotBeInstanceOf

class FrameTest: BehaviorSpec({

    given("a frame") {
        val frame = Frame.fromRolls()
        frame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
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
        )
        frame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
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
        )
        frame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
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

    given("a frame with pins knocked down") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(1),
        )
        frame.shouldBeInstanceOf<Frame.FrameWithNoBonus>()
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
        )
        frame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
        then("the bonus for 'spare' is next roll's number of pins knocked down") {
            frame.bonus shouldBe 5
            frame.score shouldBe 15
        }
    }

    given("A spare without information about next roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(2),
            roll2 = Roll(8)
        )
        frame.shouldBeInstanceOf<Frame.FrameWithSpareBonus>()
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

    given("A frame with one roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(pinsKnockedDown = 10)
        )
        `when`("all 10 pins are knocked down") {
            then("this frame is marked as strike") {
                frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
            }
            then ("strike is completed with one roll") {
                frame.roll1
                frame.shouldNotBeInstanceOf<WithSecondRoll>()
            }
        }
        `when`("it is marked as strike") {
            frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
            then("it is not marked as spare") {
                frame.shouldNotBeInstanceOf<Frame.FrameWithSpareBonus>()
            }
        }
    }

    given("A strike with information about next two rolls") {
        val frame = Frame.fromRolls(
            roll1 = Roll(10),
            nextRoll = Roll(1),
            nextSecondRoll = Roll(4)
        )
        frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
        then("the bonus is the number of pins knocked down on the following two rolls") {
            frame.bonus shouldBe 5
            frame.score shouldBe 15
        }
    }

    given("A strike with information about next roll") {
        val frame = Frame.fromRolls(
            roll1 = Roll(10),
            nextRoll = Roll(1),
        )
        frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
        then("the bonus is the number of pins knocked down on the following roll") {
            frame.bonus shouldBe 1
            frame.score shouldBe 11
        }
    }

    given("A strike without information about next rolls") {
        val frame = Frame.fromRolls(
            roll1 = Roll(10),
        )
        frame.shouldBeInstanceOf<Frame.FrameWithStrikeBonus>()
        then("no bonus is added") {
            frame.bonus shouldBe 0
            frame.score shouldBe 10
        }
    }
})