package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object MySlick extends Dao[RealPlayer, Long] with LazyLogging {

  case class PlayerData(name: String, id: Long = 0L)

  class PlayerTable(tag: Tag) extends Table[PlayerData](tag, "PLAYERS") {
    def name = column[String]("Playername")

    def id = column[Long]("PlayerID", O.PrimaryKey, O.AutoInc)

    def * = (name, id).mapTo[PlayerData]
  }

  var playerQuery = TableQuery[PlayerTable]
  lazy val playerReturnValue = playerQuery returning playerQuery.map(_.id)

  val connectionString = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  implicit val database = Database.forURL(connectionString, driver = "org.h2.Driver")


  val setup = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (playerQuery.schema).create)
  Await.result(database.run(setup), Duration.Inf)

  override def create(player: RealPlayer): Long = {
    println("create")

    val PlayerId: Long = Await.result(database.run(playerReturnValue += PlayerData(player.name)), Duration.Inf)
    //logger.info("PlayerID: " + PlayerId.toString)
    PlayerId
  }

  override def read(id: Long): RealPlayer = {
    val pq = playerQuery.filter(_.id === id).map{ RealPlayer => (RealPlayer.name)}
    val playerFuture = Await.result(database.run(pq.result), Duration.Inf)
    logger.info("playerfuture " + playerFuture)
    RealPlayer(playerFuture.last)
  }

  override def delete(id: Long): Unit = {
    ???
  }
}