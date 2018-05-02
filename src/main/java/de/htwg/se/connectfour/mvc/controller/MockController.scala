package de.htwg.se.connectfour.mvc.controller

import akka.actor.ActorSystem
import de.htwg.se.connectfour.mvc.model.{Cell, Grid, GridImpl}
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

class MockController extends Controller {

  private var _grid: Grid = new GridImpl(7, 6)

  override def createEmptyGrid(columns: Int, rows: Int): Unit = {
    _grid = new GridImpl(columns, rows)
  }

  override def columns: Int = grid.columns

  override def rows: Int = grid.rows

  override def undo(): Unit = {}

  override def redo(): Unit = {}

  override def cell(col: Int, row: Int): Cell = new Cell(col, row)

  override def statusText: String = "Sample text"

  override def checkAddCell(column: Int, cellType: CellType): Unit = {}

  override def isColumnFull(column: Int): Boolean = false

  override def removeSymbolFromColumn(column: Int): Unit = {}

  override def gameFinished: Boolean = false

  override def grid: Grid = _grid

  override def setActorSystem(actorSystem: ActorSystem): Unit = ???
}
