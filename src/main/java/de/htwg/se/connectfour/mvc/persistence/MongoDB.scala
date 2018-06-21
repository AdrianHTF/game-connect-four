package de.htwg.se.connectfour.mvc.persistence

import com.mongodb.{DBObject}
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.mongodb.scala.{MongoCredential, ServerAddress}


object MongoDB extends Dao[Cell, Long] with LazyLogging {
  type T = DBObject

  val SERVER = "192.168.99.100"
  val PORT = 27017
  val DATABASE = "GameSession"
  val COLLECTION = "GameSave"
  val server = new ServerAddress(SERVER, PORT)

  val credentials = MongoCredential.createCredential("root", "admin", "1234hot5".toArray)
  val mongoClient = MongoClient(server, List(credentials))
  val db = mongoClient.getDB(DATABASE).getCollection(COLLECTION)

  override def create(cell: Cell): T = {

    val id = (cell.x, cell.y).toString
    val builder = MongoDBObject.newBuilder //collection.insertOne(Document("_id" -> id, "cell" -> cell.cellType.toString))
    builder += "_id" -> id
    builder += "cell" -> cell.cellType
    val cellObj: DBObject = builder.result()
    logger.info(s"Created cell $id (${cell.x}, ${cell.y})")

    cellObj

  }

  override def read(): Seq[Cell] = ???
}
