package de.htwg.se.connectfour.mvc.player

import de.htwg.se.connectfour.mvc.controller.GridController
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import de.htwg.se.connectfour.mvc.view.GamingPlayers
import de.htwg.se.connectfour.mvc.model.types.CellType
import org.specs2.mutable.Specification


class GamingPlayersTest extends Specification {
  /*
  "Gaming Players" should {
    val player1 = RealPlayer("David")
    val player2 = RealPlayer("Marek")
    val gridController = GridController(2, 3)
    val gamingPlayers = new GamingPlayers(player1, player2, gridController)
    "be set right" in {
      gamingPlayers.isFirstGoing must be_==(true)
    }
    "let player1 start the game" in {
      gamingPlayers.currentPlayer must be_==(player1)
    }
    "let player2 make the 2nd turn" in {
      gamingPlayers.previousPlayer must be_==(player2)
    }
    "be able to retrieve Celltype fot current player" in {
      gamingPlayers.currentPlayerCellType must be_==(CellType.FIRST)
    }
    "change Player after a turn" in {
      val localGridController = GridController(2, 3)
      val localGamingPlayers = new GamingPlayers(player1, player2, localGridController)
      localGamingPlayers.applyTurn(0)
      localGamingPlayers.currentPlayer must be_==(player2)
    }
  }
*/
}
