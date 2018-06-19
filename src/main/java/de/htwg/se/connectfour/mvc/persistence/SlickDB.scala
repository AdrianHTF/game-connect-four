package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// PlayerDB *could* be used to save and load player names, but we don't use it yet.
object PlayerDB extends Dao[RealPlayer, Long] with LazyLogging {
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

  override def create(player: RealPlayer): T = {
    val PlayerId: T = Await.result(database.run(playerReturnValue += PlayerData(player.name)), Duration.Inf)
    PlayerId
  }

  override def read(): Seq[RealPlayer] =
    Await.result(database.run(playerQuery.all), Duration.Inf).map(source => RealPlayer(source.name))
}

object CellDB extends Dao[Cell, Long] with LazyLogging {
  type T = Long

  val connectionString = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  implicit val database = Database.forURL(connectionString, driver = "org.h2.Driver")

  object cellQuery extends TableQuery[CellTable](tag ⇒ new CellTable(tag)) {
    def all = cellQuery.result
  }
  lazy val cellReturnValue = cellQuery returning cellQuery.map(_.id)

  val setupCell = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (cellQuery.schema).create)
    Await.result(database.run(setupCell), Duration.Inf)

  override def create(cell: Cell): T = {
    val cellId: T = Await.result(database.run(cellReturnValue += CellData(cell.x, cell.y, cell.cellType.toString)), Duration.Inf)
    cellId
  }

  override def read(): Seq[Cell] =
    Await.result(database.run(cellQuery.all), Duration.Inf).map(source => Cell(source.x, source.y, Cell.toCellType(source.cellType)))
}