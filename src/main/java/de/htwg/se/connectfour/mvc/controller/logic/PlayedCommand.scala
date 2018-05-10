package de.htwg.se.connectfour.mvc.controller.logic

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.Main
import de.htwg.se.connectfour.mvc.model.Grid
import de.htwg.se.connectfour.mvc.model.types.CellType
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

case class PlayedCommand(col: Int, row: Int, cellType: CellType, grid: Grid) extends Command with LazyLogging {

  override def execute(): Unit = {
    if (Main.debug.filter) logger.info("execute. col: " + col + ", row: " + row + ", cellType:" + cellType.toString)
    grid.set(col, row, cellType)
  }

  override def undo(): Unit = {
    grid.set(col, row, CellType.EMPTY)
  }

  override def redo(): Unit = execute()

}
