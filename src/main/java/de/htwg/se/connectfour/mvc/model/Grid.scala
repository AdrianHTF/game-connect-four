package de.htwg.se.connectfour.mvc.model

import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

trait Grid {

  val MAX_COLUMN: Int
  val MAX_ROW: Int

  def columns: Int

  def rows: Int

  def emptyGrid(): Unit

  def set(x: Int, y: Int, cellType: CellType): Unit

  def setupCell(cell: Cell): Unit

  def cell(x: Int, y: Int): Cell

  def isColumnValid(column: Int): Boolean

  def isRowValid(row: Int): Boolean

  def isFull: Boolean

  def toString: String

  def toHtml: String
}
