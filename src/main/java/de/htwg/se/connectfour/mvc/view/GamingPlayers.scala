package de.htwg.se.connectfour.mvc.view

import de.htwg.se.connectfour.mvc.controller.{ Controller, PlayerGridChanged }
import de.htwg.se.connectfour.mvc.model.player.Player
import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

import scala.swing.Reactor

class GamingPlayers(firstPlayer: Player, var secondPlayer: Player, controller: Controller) extends Reactor {

  listenTo(controller)
  reactions += {
    case _: PlayerGridChanged => changePlayer()
  }

  private var _isFirstGoing = true

  def isFirstGoing: Boolean = _isFirstGoing

  def currentPlayer: Player = if (_isFirstGoing) firstPlayer else secondPlayer

  def previousPlayer: Player = if (!_isFirstGoing) firstPlayer else secondPlayer

  private def changePlayer(): Unit = {
    _isFirstGoing = !_isFirstGoing
  }

  def applyTurn(column: Int): Unit = {
    controller.checkAddCell(column, currentPlayerCellType)
  }

  def setSecondPlayer(second: Player): Unit = {
    secondPlayer = second
  }

  def currentPlayerCellType: CellType = cellType(currentPlayer)

  private[this] def cellType(player: Player): CellType = {
    if (player == firstPlayer) CellType.FIRST else CellType.SECOND
  }
}
