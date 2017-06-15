package de.htwg.se.connectfour.mvc.controller

import com.google.inject.Inject
import com.google.inject.name.Named
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.logic.{CheckWinner, PlayedCommand, RevertManager, Validator}
import de.htwg.se.connectfour.mvc.model.{Cell, Grid, GridImpl}
import de.htwg.se.connectfour.types.CellType.CellType
import de.htwg.se.connectfour.types.StatusType.GameStatus
import de.htwg.se.connectfour.types.{CellType, StatusType}

import scala.swing.Publisher

case class GridController @Inject() (@Named("columns") columns: Int, @Named("rows") rows: Int) extends Publisher with Controller with LazyLogging {

  private val revertManager = new RevertManager

  private var _grid: Grid = _
  private var gameStatus: GameStatus = StatusType.NEW
  private var checkWinner: CheckWinner = _
  private var validator: Validator = _
  private var _gameFinished = false

  createEmptyGrid(columns, rows)

  override def createEmptyGrid(columns: Int, rows: Int): Unit = {
    logger.debug("created empty grids")
    _grid = new GridImpl(columns, rows)
    _gameFinished = false
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

  override def cell(col: Int, row: Int): Cell = _grid.cell(col, row)

  override def statusText: String = StatusType.message(gameStatus)

  override def checkAddCell(column: Int, cellType: CellType): Unit = {
    if (isInvalid(column)) return
    addCell(column, cellType)
    checkFinish(column)
  }

  private def isInvalid(column: Int) = gameFinished || isColumnFull(column) || !isColumnValid(column)

  private def addCell(column: Int, cellType: CellType): Unit = {
    logger.info("added cell of type " +cellType+" at: " + column)
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
    val hasWon = checkWinner.checkForWinner(columnMove, rowMove)
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
