package de.htwg.se.connectfour.mvc.persistence

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import de.htwg.se.connectfour.mvc.controller.{Controller}

object MongoDB extends DaoMongo [Cell, Long] with LazyLogging {
  type T = Unit

  val log = false

  val mongoConn = MongoConnection()
  val mongoColl = mongoConn("casbah_test")("test_data")

  override def save(cell: Cell): T = {
    //mongoColl.drop   possible delete
    mongoColl += MongoDBObject("x" -> cell.x, "y" -> cell.y, "typ" -> cell.cellType.toString)
  }

  override def load(controller: Controller): Unit = {

    for { c <- mongoColl} {
      val x = Int.unbox(c.get("x"))
      val y = Int.unbox(c.get("y"))
      val typ = Cell.toCellType(String.valueOf(c.get("typ")))
      controller.grid.set(x, y, typ)
    }
  }
}