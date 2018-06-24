package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// PlayerDB *could* be used to save and load player names, but we don't use it yet.
object Slick_PlayerDB extends Dao[RealPlayer, Long] with LazyLogging {
  type T = Long

  object playerQuery extends TableQuery[PlayerTable](tag ⇒ new PlayerTable(tag)) {
    def all = playerQuery.result
  }
  lazy val playerReturnValue = playerQuery returning playerQuery.map(_.id)

  val connectionString = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  implicit val database = Database.forURL(connectionString, driver = "org.h2.Driver")

  val setupPlayer = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (playerQuery.schema).create)
  Await.result(database.run(setupPlayer), Duration.Inf)

  override def save(player: RealPlayer): T = {
    val PlayerId: T = Await.result(database.run(playerReturnValue += PlayerData(player.name)), Duration.Inf)
    PlayerId
  }

  override def load(): Seq[RealPlayer] =
    Await.result(database.run(playerQuery.all), Duration.Inf).map(source => RealPlayer(source.name))
}
