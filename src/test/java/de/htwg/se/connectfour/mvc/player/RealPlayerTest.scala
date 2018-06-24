package de.htwg.se.connectfour.mvc.player

import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import org.specs2.mutable.Specification

class RealPlayerTest extends Specification {
  "A real player" should {
    val player = RealPlayer("testPlayer")
    "be displayed as real player" in {
      player.isReal must be_==(true)
    }
  }

}
