package de.htwg.se.connectfour.mvc.persistence

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject


object MongoDB extends Dao[Cell, Long] with LazyLogging {
  type T = Unit

  val mongoConn = MongoConnection()
  val mongoColl = mongoConn("casbah_test")("test_data")

  override def create(cell: Cell): T = {

    val id = (cell.x, cell.y).toString

    //Thread.sleep(5000)
    mongoColl += MongoDBObject("id" -> id, "cell" -> cell.cellType.toString)
    logger.info(s"MongoDB: Stored $id")
  }

  override def read(): Seq[Cell] = {

    for (dbCell <- mongoColl) {
      logger.info(dbCell.toString)
    }
    ???
  }
}
