package de.htwg.se.connectfour.mvc.persistence

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import org.mongodb.scala.bson.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, SingleObservable}

object MongoDB extends Dao[Cell, Long] with LazyLogging {
  type T = SingleObservable[Completed]

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("connect_four")
  val collection: MongoCollection[Document] = database.getCollection("mongo")

  override def create(cell: Cell): T = {
    val id = (cell.x, cell.y).toString
    val cellId = collection.insertOne(Document("_id" -> id, "cell" -> cell.cellType.toString))
    logger.info(s"Created cell $id (${cell.x}, ${cell.y})")
    cellId
  }

  override def read(): Seq[Cell] = ???
}
