package de.htwg.se.connectfour.mvc.model

import de.htwg.se.connectfour.mvc.model.types.CellType
import org.specs2.mutable.Specification

import scala.xml.dtd.EMPTY

class CellTest extends Specification {

  "A new Cell" should {
    val cell = new Cell(1, 2)

    "be empty if not set" in {
      cell.cellType must be_==(CellType.EMPTY)
    }

    "the empty symbol must be a space" in {
      cell.toString must be_==(" ")
    }

    val empty = " "
    val first = "O"
    val second = "X"
    val typeEmpty = Cell.toCellType(empty)
    val typeFirst = Cell.toCellType(first)
    val typeSecond = Cell.toCellType(second)

    "empty symbol converted into celltype" in {
      typeEmpty must be_== (types.CellType.EMPTY)
    }
    "O symbol converted into celltype" in {
      typeFirst must be_== (types.CellType.FIRST)
    }
    "X symbol converted into celltype" in {
      typeSecond must be_== (types.CellType.SECOND)
    }

  }
}
