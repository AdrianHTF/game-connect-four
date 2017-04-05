package de.htwg.se.connectfour.logic

import de.htwg.se.connectfour.model.{Cell, Grid}

class CheckWinner(val grid: Grid) {
  val NUMBER_OF_CELLS_TO_WIN = 4
  val CELLS_AROUND_TO_WIN: Int = NUMBER_OF_CELLS_TO_WIN - 1

  def checkForWinner(cell: Cell): Boolean = {
    val checkRound = new CheckTurnWinner(cell)
    checkRound.checkVertical() || checkRound.checkHorizontal() || checkRound.checkDiagonal()
  }

  private class CheckTurnWinner(val cell: Cell) {

    def checkVertical(): Boolean = {
      var winCounter = 0
      for (i <- cell.y to cell.y + CELLS_AROUND_TO_WIN) {
        if (grid.isCellValid(cell.x, cell.y + CELLS_AROUND_TO_WIN) && grid.cell(cell.x, i).cellType == cell.cellType)
          winCounter += 1
        else
          winCounter = 0
      }
      winCounter == NUMBER_OF_CELLS_TO_WIN
    }

    def checkHorizontal(): Boolean = {
      var winCounter = 0
      for (i <- cell.x - CELLS_AROUND_TO_WIN to cell.x + CELLS_AROUND_TO_WIN) {
        if (grid.isCellValid(i, cell.y) && (grid.cell(i, cell.y).cellType == cell.cellType))
          winCounter += 1
        else if (winCounter != NUMBER_OF_CELLS_TO_WIN)
          winCounter = 0
      }
      winCounter == NUMBER_OF_CELLS_TO_WIN
    }

    def checkDiagonal(): Boolean = {
      leftUpToRightDown() || leftDownToRightUp()
    }

    def leftDownToRightUp(): Boolean = {
      var winCounter = 0
      for (i <- -CELLS_AROUND_TO_WIN to CELLS_AROUND_TO_WIN) {
        {
          if (grid.isCellValid(cell.x - i, cell.y + i) && grid.cell(cell.x - i, cell.y + i).cellType == cell.cellType)
            winCounter += 1
          else if (winCounter != NUMBER_OF_CELLS_TO_WIN)
            winCounter = 0
        }
      }
      winCounter == NUMBER_OF_CELLS_TO_WIN
    }

    def leftUpToRightDown(): Boolean = {
      var winCounter = 0
      for (i <- -CELLS_AROUND_TO_WIN to CELLS_AROUND_TO_WIN) {
        {
          if (grid.isCellValid(cell.x + i, cell.y + i) && grid.cell(cell.x + i, cell.y + i).cellType == cell.cellType) {
            winCounter += 1
          } else if (winCounter != NUMBER_OF_CELLS_TO_WIN)
            winCounter = 0
        }
      }
      winCounter == NUMBER_OF_CELLS_TO_WIN
    }
  }

}
