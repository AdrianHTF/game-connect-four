package de.htwg.se.connectfour.mvc.persistence

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import de.htwg.se.connectfour.mvc.controller.{Controller}

object MongoDB extends DaoMongo [Cell, Long] with LazyLogging {
  type T = Unit

  val mongoConn = MongoConnection()
  val mongoColl = mongoConn("casbah_test")("test_data")

  override def create(cell: Cell): T = {
    mongoColl += MongoDBObject("x" -> cell.x, "y" -> cell.y, "typ" -> cell.cellType.toString)
    //logger.info(s"MongoDB: Saved ${cell.debug}")
  }

  override def read(controller: Controller): Unit = {

    val seq = Seq.empty[Cell]

    for { c <- mongoColl} {
      //logger.info(s"Old seq: ${seq.size}")
      //logger.info(s"c: $c")
      val x = Int.unbox(c.get("x"))
      //logger.info(s"x: $x")
      val y = Int.unbox(c.get("y"))
      //logger.info(s"y: $y")
      val typ = Cell.toCellType(String.valueOf(c.get("typ")))
      //logger.info(s"typ: $typ")
      val cell = Cell(x, y, typ)
      //logger.info(s"cell: ${cell.debug}")
      seq :+ cell
      //logger.info(s"New seq: ${seq.size}\n")
      controller.grid.set(x, y, typ)
    }

    logger.info(s"Mongo read returning ${seq.size} cells")

  }

}