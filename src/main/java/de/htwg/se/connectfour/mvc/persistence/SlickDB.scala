package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object PlayerDB extends Dao[RealPlayer, Long] with LazyLogging {

  var playerQuery = TableQuery[PlayerTable]
  lazy val playerReturnValue = playerQuery returning playerQuery.map(_.id)

  val connectionString = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  implicit val database = Database.forURL(connectionString, driver = "org.h2.Driver")

  val setupPlayer = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (playerQuery.schema).create)
  Await.result(database.run(setupPlayer), Duration.Inf)

  override def create(player: RealPlayer): Long = {
    val PlayerId: Long = Await.result(database.run(playerReturnValue += PlayerData(player.name)), Duration.Inf)
    PlayerId
  }

  override def read(id: Long): RealPlayer = {
    val pq = playerQuery.filter(_.id === id).map{ RealPlayer => (RealPlayer.name)}
    val playerFuture = Await.result(database.run(pq.result), Duration.Inf)
    RealPlayer(playerFuture.last)
  }
}

object CellDB extends Dao[Cell, Long] with LazyLogging {



  val connectionString = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  implicit val database = Database.forURL(connectionString, driver = "org.h2.Driver")

  var cellQuery = TableQuery[CellTable]
  lazy val cellReturnValue = cellQuery returning cellQuery.map(_.id)

  val setupCell = DBIO.seq(
    // Create the tables, including primary and foreign keys
    (cellQuery.schema).create)
    Await.result(database.run(setupCell), Duration.Inf)

  override def create(cell: Cell): Long = {
    val cellId: Long = Await.result(database.run(cellReturnValue += CellData(cell.x, cell.y, cell.cellType.toString)), Duration.Inf)
    cellId
  }

  override def read(id: Long): Cell = {

    val cq = cellQuery.filter(_.id === id).map(Cell => (Cell.x, Cell.y, Cell.cellType))
    //
    val cellFuture = database.run(cq.result)
    var returnCell : Cell = ???
    cellFuture.onComplete({
      case Success(cellSeq) => {
        val cell = cellSeq.head
        val cellType = Cell.toCellType(cell._3)

        returnCell = Cell(cell._1, cell._2, cellType)
      }
      case Failure(cellSeq) => {}
    })

    returnCell
  }
}