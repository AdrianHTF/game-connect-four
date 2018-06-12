package de.htwg.se.connectfour.mvc.model

import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

/**
 * Gaming 2D grid with starting coordinates in left upper corner.
 *
 * Let's have grid size 3x2, X on position [2,0] and O on [1,1]:
 *
 * |   |   | X |
 * +---+---+---+
 * |   | O |   |
 * +---+---+---+
 * | 0 | 1 | 2 |
 *
 * @param columns represents width of grid (x coordinate, from 0 to columns - 1)
 * @param rows    represents height of grid (y coordinate, from 0 to rows - 1)
 *
 */
class GridImpl(val columns: Int, val rows: Int) extends Grid {

  override val MAX_COLUMN: Int = columns - 1
  override val MAX_ROW: Int = rows - 1

  private val cells: Array[Array[Cell]] = Array.ofDim[Cell](columns, rows)
  emptyGrid()

  override def emptyGrid(): Unit = {
    for (row <- 0 until rows; column <- 0 until columns) {
      cells(column)(row) = new Cell(column, row)
    }
  }

  override def set(x: Int, y: Int, cellType: CellType): Unit = cells(x)(y) = Cell(x, y, cellType)

  override def setupCell(cell: Cell): Unit = cells(cell.x)(cell.y) = cell

  override def cell(x: Int, y: Int): Cell = cells(x)(y)

  override def isColumnValid(column: Int): Boolean =
    0 <= column && column <= MAX_COLUMN

  override def isRowValid(row: Int): Boolean =
    0 <= row && row <= MAX_ROW

  override def isFull: Boolean = {
    for (row <- 0 until rows; column <- 0 until columns) {
      if (cells(column)(row).cellType == CellType.EMPTY) return false
    }
    true
  }

  override def toString: String = {
    val gridInString = buildGridInString()
    addColumnNumbers(gridInString)
  }

  private[this] def buildGridInString(): String = {
    var result = "\n"
    for (y <- 0 until rows) {
      for (x <- 0 until columns) {
        result += "|%2s ".format(cells(x)(y))
      }
      result += "|\n"
      result += "+---" * columns + "+\n"
    }
    result
  }

  private[this] def addColumnNumbers(partialString: String): String = {
    var result = partialString
    for (x <- 0 until columns) {
      result += "|%2s ".format(x)
    }
    result += "|\n"
    result
  }

  override def toHtml: String = "<p  style=\"font-family:'Lucida Console', monospace\"> " + toString.replace("\n", "<br>").replace("  ", " _") + "</p>"
}
