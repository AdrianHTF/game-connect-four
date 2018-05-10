package de.htwg.se.connectfour.mvc.controller

import akka.actor.{ ActorRef, ActorSystem }
import de.htwg.se.connectfour.mvc.model.{ Cell, Grid }
import de.htwg.se.connectfour.mvc.model.types.CellType.CellType

import scala.swing.Publisher
import scala.swing.event.Event

trait Controller extends Publisher {

  def columns: Int

  def rows: Int

  def createEmptyGrid(columns: Int, rows: Int)

  def undo(): Unit

  def redo(): Unit

  def cell(col: Int, row: Int): Cell

  def statusText: String

  def checkAddCell(column: Int, cellType: CellType): Unit

  def isColumnFull(column: Int): Boolean

  def removeSymbolFromColumn(column: Int): Unit

  def gameFinished: Boolean

  def grid: Grid

  def setActorSystem(actorSystem: ActorSystem): Unit

  var actor: ActorRef
}

class PlayerGridChanged extends Event

class GridChanged extends Event

class PlayerWon extends Event

class Draw extends Event

class FilledColumn extends Event

class InvalidMove extends Event

