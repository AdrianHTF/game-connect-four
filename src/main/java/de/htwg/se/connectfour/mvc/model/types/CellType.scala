package de.htwg.se.connectfour.mvc.model.types

object CellType extends Enumeration {
  type CellType = Value
  val EMPTY = Value(" ")
  val FIRST = Value("O")
  val SECOND = Value("X")


}
