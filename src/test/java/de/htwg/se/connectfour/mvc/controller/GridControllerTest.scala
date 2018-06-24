package de.htwg.se.connectfour.mvc.controller

import de.htwg.se.connectfour.mvc.model.{Cell, GridImpl}
import de.htwg.se.connectfour.mvc.model.types.CellType
import org.specs2.mutable.Specification

import scala.xml.dtd.EMPTY


class GridControllerTest extends Specification {
  "New GridController of grid 3x2" should {

    val grid = new GridImpl(3, 2)

    val xCell = Cell(2, 0, CellType.SECOND)
    val oCell = Cell(0, 1, CellType.FIRST)
    grid.setupCell(xCell)
    grid.setupCell(oCell)

    val gridController = GridController(3, 2)


    "have statusText" in {
      val localGridController = GridController(3, 2)
      localGridController.statusText must be_==("A new game was created")
    }

    "have valid and not full column" in {
      gridController.isColumnFull(0) must be_==(false)
    }

    "have removed symbol from column" in {
      val localGridController = GridController(3, 2)
      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.removeSymbolFromColumn(0)
      localGridController.cell(0, 0).cellType must be_==(CellType.EMPTY)
    }


    "have won with this move (horizontal)" in {
      val localGridController = GridController(7, 6)
      for (_ <- 0 until 4) {
        localGridController.checkAddCell(0, CellType.FIRST)
      }
      localGridController.gameFinished must be_==(true)
    }

    "have also won with this move (vertical)" in {
      val localGridController = GridController(7, 6)
      for (column <- 0 until 4) {
        localGridController.checkAddCell(column, CellType.FIRST)
      }

      localGridController.gameFinished must be_==(true)
    }

    "have also won with this move (diagonal)" in {
      val localGridController = GridController(7, 6)
      var counter = 0
      //setting up 3 cells
      for (column <- 2 until 5) {
        localGridController.grid.set(counter, column, CellType.FIRST)
        counter += 1
      }
      //adds last cell to and checks if someone won
      localGridController.checkAddCell(3, CellType.FIRST)
      localGridController.gameFinished must be_==(true)
    }

    "have also won with this move (diagonal)" in {
      val localGridController = GridController(7, 6)
      var counter = 3
      //setting up 3 cells
      for (column <- 2 until 5) {
        localGridController.grid.set(counter, column, CellType.FIRST)
        counter -= 1
      }
      //adds last cell to and checks if someone won
      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.gameFinished must be_==(true)
    }

    "undo removes last draw" in {
      val localGridController = GridController(3, 2)

      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.undo()
      localGridController.cell(0, 0).cellType must be_== (CellType.EMPTY)
    }

    "redo" in {
      val localGridController = GridController(3, 2)

      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.undo()
      localGridController.redo()
      localGridController.cell(0, 1).cellType must be_== (CellType.FIRST)
    }

    "check is column full" in {
      val localGridController = GridController(3, 2)
      localGridController.isColumnFull(0) must be_== (false)
      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.checkAddCell(0, CellType.SECOND)
      localGridController.checkAddCell(0, CellType.FIRST)
      localGridController.checkAddCell(0, CellType.SECOND)
      localGridController.isColumnFull(0) must be_== (true)
    }
  }
}
