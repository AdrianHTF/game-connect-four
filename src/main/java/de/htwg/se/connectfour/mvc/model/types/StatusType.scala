package de.htwg.se.connectfour.mvc.model.types

object StatusType extends Enumeration {
  type GameStatus = Value
  val STARTED, NEW, SET, FULL, INVALID, DRAW, UNDO, REDO, FINISHED = Value

  val gameStatusMap: Map[GameStatus, String] = Map[GameStatus, String](
    NEW -> "A new game was created",
    SET -> "A cell was set",
    FULL -> "Column is full",
    INVALID -> "Invalid command",
    DRAW -> "Draw game",
    UNDO -> "One step reverted",
    REDO -> "One step reapplied",
    FINISHED -> "Game is finished"
  )

  def message(gameStatus: GameStatus): String = {
    gameStatusMap(gameStatus)
  }

}
