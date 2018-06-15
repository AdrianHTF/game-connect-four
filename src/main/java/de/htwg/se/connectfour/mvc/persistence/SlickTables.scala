package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._

case class PlayerData(name: String, id: Long = 0L)
case class CellData(x: Int, y: Int, cellType : String, id: Long = 0L)




class CellTable(tag: Tag) extends Table[CellData] (tag, _tableName = "CELLS") {
  def x = column[Int]("X")
  def y = column[Int]("Y")
  def cellType = column[String]("TYPE")
  def id = column[Long]("PlayerID", O.PrimaryKey, O.AutoInc)
  def * = (x, y, cellType, id).mapTo[CellData]
}


class PlayerTable(tag: Tag) extends Table[PlayerData](tag, "PLAYERS") {
  def name = column[String]("Playername")
  def id = column[Long]("PlayerID", O.PrimaryKey, O.AutoInc)
  def * = (name, id).mapTo[PlayerData]
}



