package de.htwg.se.connectfour.mvc.controller

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.google.inject.Inject
import com.google.inject.name.Named
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.Main
import de.htwg.se.connectfour.mvc.controller.logic.{ CheckWinner, PlayedCommand, RevertManager, Validator }
import de.htwg.se.connectfour.mvc.model.{ Cell, Grid, GridImpl }
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType
import de.htwg.se.connectfour.mvc.model.types.StatusType.GameStatus
import de.htwg.se.connectfour.mvc.model.types.{ CellType, StatusType }
import de.htwg.se.connectfour.mvc.persistence.CellDB

import scala.swing.Publisher

case class Move(col: Int, ct: CellType, gridController: Controller)

class GridControllerActor extends Actor with LazyLogging {
  def receive = {
    case Move(col, ct, gridController) => {
      if (Main.debug.filter) logger.info("GridControllerActor Move received. col = " + col + ", type = " + ct.toString)
      gridController.checkAddCell(col, ct);
    }
  }
}

case class GridController @Inject() (@Named("columns") columns: Int, @Named("rows") rows: Int) extends Publisher with Controller with LazyLogging {

  val cellDB = CellDB

  private var revertManager: RevertManager = _

  private var _grid: Grid = _
  private var gameStatus: GameStatus = StatusType.NEW
  private var checkWinner: CheckWinner = _
  private var validator: Validator = _
  private var _gameFinished = false

  private var _system: ActorSystem = _
  var actor: ActorRef = _

  createEmptyGrid(columns, rows)

  object cells {
    val cells = cellDB.read
    //cells
  }

  override def setActorSystem(system: ActorSystem): Unit = {
    _system = system
    actor = _system.actorOf(Props[GridControllerActor], "GridControllerActor")
  }

  override def createEmptyGrid(columns: Int, rows: Int): Unit = {
    if (Main.debug.filter) logger.debug("created empty grids")
    _grid = new GridImpl(columns, rows)
    _gameFinished = false
    revertManager = new RevertManager
    checkWinner = CheckWinner(_grid)
    validator = Validator(_grid)
    gameStatus = StatusType.NEW
    publish(new GridChanged)
  }

  override def undo(): Unit = {
    logger.info("undid last move")
    val didUndo = revertManager.undo()
    if (didUndo) {
      gameStatus = StatusType.UNDO
      publish(new PlayerGridChanged)
    } else {
      gameStatus = StatusType.INVALID
      publish(new InvalidMove)
    }
  }

  override def redo(): Unit = {
    logger.info("redid move")
    val didRedo = revertManager.redo()
    if (didRedo) {
      gameStatus = StatusType.REDO
      publish(new PlayerGridChanged)
    } else {
      gameStatus = StatusType.INVALID
      publish(new InvalidMove)
    }
  }

  override def saveGame(): Unit = {
    var count = 0
    for (row <- 0 until grid.rows - 1; column <- 0 until grid.columns - 1){
      cellDB.create(grid.cell(row, column))
      val cellType = grid.cell(row, column).cellType.toString
      //logger.info(s"Saved cell #$count: $row, $column ($cellType)")
      count += 1
    }
    logger.info(s"Saved $count cells")
  }

  override def loadGame(): Unit = {

    val cells = cellDB.read

    val width = cells.map(_.x).max + 2
    val height = cells.map(_.y).max + 2
    createEmptyGrid(height, width)

    var count = 0
    cells.foreach(cell => {
      grid.set(cell.x, cell.y, cell.cellType)
      //logger.info(s"Cell #$count loaded: ${cell.x}, ${cell.y}, ${cell.cellType}")
      count += 1
    })
    gameStatus = StatusType.SET

    logger.info(s"Loaded Grid of size $width, $height ($count cells)")

    publish(new GridChanged)

    val player1moves = cells.count(cell => cell.cellType == CellType.FIRST)
    val player2moves = cells.count(cell => cell.cellType == CellType.SECOND)

    if (player2moves < player1moves) publish(new PlayerGridChanged)
  }

  override def cell(col: Int, row: Int): Cell = _grid.cell(col, row)

  override def statusText: String = StatusType.message(gameStatus)

  override def checkAddCell(column: Int, cellType: CellType): Unit = {
    if (Main.debug.filter) logger.info("checkAddCell. isInvalid? " + isInvalid(column))
    if (isInvalid(column)) return
    addCell(column, cellType)
    checkFinish(column)
  }

  private def isInvalid(column: Int) = gameFinished || isColumnFull(column) || !isColumnValid(column)

  private def addCell(column: Int, cellType: CellType): Unit = {
    if (Main.debug.filter) logger.info("addCell-method of type " + cellType + " at: " + column)
    //val move = Move(column, validator.lastRowPosition(column),cellType, this)
    revertManager.execute(PlayedCommand(column, validator.lowestEmptyRow(column), cellType, grid))
    gameStatus = StatusType.SET
    publish(new PlayerGridChanged)
  }

  private def isColumnValid(column: Int): Boolean = {
    val valid = _grid.isColumnValid(column)
    if (!valid) {
      logger.warn("tried to insert into invalid column: " + column)
      gameStatus = StatusType.INVALID
      publish(new InvalidMove)
    }
    valid
  }

  override def isColumnFull(column: Int): Boolean = {
    val isFull = validator.isColumnFull(column)
    if (isFull) {
      logger.warn("tried to insert into full column:  " + column)
      gameStatus = StatusType.FULL
      publish(new FilledColumn)
    }
    isFull
  }

  override def removeSymbolFromColumn(column: Int): Unit = {
    val lastFilledRow = validator.lastRowPosition(column)
    _grid.setupCell(Cell(column, lastFilledRow, CellType.EMPTY))
  }

  private def checkFinish(columnMove: Int): Unit = {
    val rowMove = validator.lastRowPosition(columnMove)
    if (Main.debug.filter) logger.info("checkFinish(). columnMove = " + columnMove + ", rowMove = " + rowMove)
    val hasWon = checkWinner.checkForWinner(columnMove, rowMove)
    if (Main.debug.filter) logger.info("checkFinish hasWon = " + hasWon)

    if (hasWon) {
      logger.info("game has finished")
      gameStatus = StatusType.FINISHED
      _gameFinished = true
      publish(new PlayerWon)
    } else if (grid.isFull) {
      logger.info("game has finished with draw")
      gameStatus = StatusType.DRAW
      _gameFinished = true
      publish(new Draw)
    }
  }

  override def gameFinished: Boolean = _gameFinished

  override def grid: Grid = _grid
}
