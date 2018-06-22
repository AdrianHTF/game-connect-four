package de.htwg.se.connectfour.mvc.persistence

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject

import scala.collection.mutable

object MongoDB extends Dao[Cell, Long] with LazyLogging {
  type T = Unit

  val mongoConn = MongoConnection()
  val mongoColl = mongoConn("casbah_test")("test_data")

  override def create(cell: Cell): T = {
    mongoColl += MongoDBObject("x" -> cell.x, "y" -> cell.y, "typ" -> cell.cellType.toString)
    logger.info(s"MongoDB: Saved ${cell.debug}")
  }

  override def read(): Seq[Cell] = {

    /*
    val seq = Seq.empty[Cell]//  Seq[Cell] = null

    for (dbCell <- mongoColl) {
      logger.info(dbCell.toString)

    }
    val i = 2
    */

    //val seq = mongoColl.map(item => Cell(1, 1, CellType.EMPTY))

    //val seq = mongoColl.map(item => item.)

    val seq = Seq.empty[Cell]

    for { c <- mongoColl} {
      logger.info(s"Old seq: ${seq.size}")
      logger.info(s"c: $c")
      val x = Int.unbox(c.get("x"))
      logger.info(s"x: $x")
      val y = Int.unbox(c.get("y"))
      logger.info(s"y: $y")
      val typ = Cell.toCellType(String.valueOf(c.get("typ")))
      logger.info(s"typ: $typ")
      val cell = Cell(x, y, typ)
      logger.info(s"cell: ${cell.debug}")
      seq :+ cell
      logger.info(s"New seq: ${seq.size}\n")
    }

    logger.info(s"Mongo read returning ${seq.size} cells")

    seq
  }
}
