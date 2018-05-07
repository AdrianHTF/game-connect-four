package de.htwg.se.connectfour.mvc.view

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.controller.{Controller, Move, PlayerGridChanged}
import de.htwg.se.connectfour.mvc.model.player.Player
import de.htwg.se.connectfour.mvc.model.types
import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

import scala.swing.Reactor

class GamingPlayers(firstPlayer: Player, var secondPlayer: Player, controller: Controller, actor:ActorRef) extends Reactor with LazyLogging {

  listenTo(controller)
  reactions += {
    case _: PlayerGridChanged => changePlayer()
  }

  private var _isFirstGoing = true

  def isFirstGoing: Boolean = _isFirstGoing

  def currentPlayer: Player = if (_isFirstGoing) firstPlayer else secondPlayer

  def previousPlayer: Player = if (!_isFirstGoing) firstPlayer else secondPlayer

  private def changePlayer(): Unit = {
    logger.info("PlayerGridChanged event received (changePlayer()). Old player = " + currentPlayer.toString)
    _isFirstGoing = !_isFirstGoing
  }

  def applyTurn(column: Int): Unit = {

    //controller.checkAddCell(column, currentPlayerCellType)
    /*
    val move = Move(column, currentPlayerCellType, controller)
    logger.info("applyTurn() current player: " + currentPlayer.toString + ", column: " + column)
    actor ! move
    */
  }

  def setSecondPlayer(second: Player): Unit = {
    secondPlayer = second
  }

  def currentPlayerCellType: CellType = cellType(currentPlayer)

  private[this] def cellType(player: Player): CellType = {
    if (player == firstPlayer) {
      logger.info("cellType player: " + player.name.toString + " CellType.first")
      CellType.FIRST
    }
    else {
      logger.info("cellType player: " + player.name.toString + " CellType.second")
      CellType.SECOND
    }
  }
}
