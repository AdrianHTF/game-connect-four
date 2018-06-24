package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.model.Cell

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Slick_CellDB extends Dao[Cell, Long] with LazyLogging {
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

  override def save(cell: Cell): T = {
    //Await.result(database.run(cellQuery.delete), Duration.Inf)  possible delete
    val cellId: T = Await.result(database.run(cellReturnValue += CellData(cell.x, cell.y, cell.cellType.toString)), Duration.Inf)
    cellId
  }

  override def load(): Seq[Cell] =
    Await.result(database.run(cellQuery.all), Duration.Inf).map(source => Cell(source.x, source.y, Cell.toCellType(source.cellType)))
}