package de.htwg.se.connectfour.mvc.controller.logic

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.Main
import de.htwg.se.connectfour.mvc.model.{Cell, Grid}

case class CheckWinner(grid: Grid) extends LazyLogging{

  val validator = Validator(grid)

  val NUMBER_OF_CELLS_TO_WIN = 4
  val CELLS_AROUND_TO_WIN: Int = NUMBER_OF_CELLS_TO_WIN - 1

  def checkForWinner(col: Int, row: Int): Boolean = {
    if (Main.debug.filter) logger.info("checkForWinner called with col " + col + ", row " + row)
    val cell = grid.cell(col, row)
    if (Main.debug.filter) logger.info("checkForWinner cell = " + cell.toString)
    val checkRound = CheckTurnWinner(cell)
    checkRound.checkLine(CheckType.VERTICAL) || checkRound.checkLine(CheckType.HORIZONTAL) ||
      checkRound.checkLine(CheckType.DIAGONAL_\) || checkRound.checkLine(CheckType.DIAGONAL_/)
  }

  private object CheckType extends Enumeration {
    val VERTICAL, HORIZONTAL, DIAGONAL_/, DIAGONAL_\ = Value
  }

  private case class CheckTurnWinner(cell: Cell) {

    def checkLine(checkType: CheckType.Value): Boolean = {
      var winCounter = 0
      var isCellValidAndSame = false
      for (i <- -CELLS_AROUND_TO_WIN to +CELLS_AROUND_TO_WIN) {
        isCellValidAndSame = processCheckType(i, checkType)
        winCounter = increaseOrResetCounter(winCounter, isCellValidAndSame)
        if (winCounter == NUMBER_OF_CELLS_TO_WIN) return true
      }
      false
    }

    def processCheckType(i: Int, checkType: CheckType.Value): Boolean = {
      val cellType = cell.cellType
      checkType match {
        case CheckType.HORIZONTAL =>
          validator.isValidAndSameType(cell.x + i, cell.y, cellType)
        case CheckType.VERTICAL =>
          validator.isValidAndSameType(cell.x, cell.y + i, cellType)
        case CheckType.DIAGONAL_\ =>
          validator.isValidAndSameType(cell.x + i, cell.y + i, cellType)
        case CheckType.DIAGONAL_/ =>
          validator.isValidAndSameType(cell.x - i, cell.y + i, cellType)
      }
    }

    def increaseOrResetCounter(oldValue: Int, shouldIncrease: Boolean): Int = {
      if (shouldIncrease) oldValue + 1 else 0
    }
  }

}
