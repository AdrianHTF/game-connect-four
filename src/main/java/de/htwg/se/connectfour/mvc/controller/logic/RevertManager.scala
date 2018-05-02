package de.htwg.se.connectfour.mvc.controller.logic

import akka.actor.ActorRef
import de.htwg.se.connectfour.mvc.controller.Move
import de.htwg.se.connectfour.mvc.model.types.CellType

class RevertManager {

  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def execute(command: Command, actorRef: ActorRef, move: Move): Unit = {
    undoStack = command :: undoStack
    redoStack = Nil
    println("\n ******* actorRef null? " + (actorRef == null) + "\n")

    actorRef ! move
    //command.execute()
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
