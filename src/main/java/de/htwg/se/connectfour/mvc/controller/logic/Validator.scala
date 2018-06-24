package de.htwg.se.connectfour.mvc.controller.logic

import de.htwg.se.connectfour.mvc.model.Grid
import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

case class Validator(grid: Grid) {

  def isColumnFull(column: Int): Boolean = lowestEmptyRow(column) < 0

  def lastRowPosition(column: Int): Int = Math.min(grid.MAX_ROW, lowestEmptyRow(column) + 1)

  def lowestEmptyRow(column: Int): Int = {
    var currentRow = grid.MAX_ROW
    while (isCellValidAndNotEmpty(column, currentRow)) currentRow -= 1
    currentRow
  }

  def isValidAndSameType(x: Int, y: Int, cellType: CellType): Boolean = isCellValid(x, y) && isCellSameType(x, y, cellType)

  private def isCellValidAndNotEmpty(column: Int, row: Int): Boolean =
    isCellValid(column, row) && grid.cell(column, row).cellType != CellType.EMPTY

  private def isCellSameType(x: Int, y: Int, cellType: CellType): Boolean = grid.cell(x, y).cellType == cellType

  def isCellValid(column: Int, row: Int): Boolean = grid.isColumnValid(column) && grid.isRowValid(row)

}
