package de.htwg.se.connectfour.mvc.model

import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

case class Cell(x: Int, y: Int, cellType: CellType) {

  def this(x: Int, y: Int) {
    this(x, y, CellType.EMPTY)
  }

  override def toString: String = cellType.toString

  def debug: String = {
    x + ", " + y + ": " + cellType.toString
  }
}

object Cell {
  def toCellType (tp: String): CellType = {
    if (tp.equals (" ") )
      CellType.EMPTY
    else if (tp.equals ("O") ) {
      CellType.FIRST
    }
    else
      CellType.SECOND
  }
}

