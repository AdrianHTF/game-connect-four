package de.htwg.se.connectfour.mvc.controller.logic

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.Main

class RevertManager extends LazyLogging {

  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def execute(command: Command): Unit = {
    undoStack = command :: undoStack
    redoStack = Nil
    if (Main.debug.filter) logger.info("RevertManager.execute()")

    command.execute()
  }

  def undo(): Boolean = {
    if (undoStack.isEmpty) return false

    val head :: stack = undoStack
    head.undo()
    undoStack = stack
    redoStack = head :: redoStack
    true
  }

  def redo(): Boolean = {
    if (redoStack.isEmpty) return false

    val head :: stack = redoStack
    head.redo()
    redoStack = stack
    undoStack = head :: undoStack
    true
  }

}
