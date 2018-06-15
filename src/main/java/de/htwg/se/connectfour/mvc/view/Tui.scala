package de.htwg.se.connectfour.mvc.view

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.Main
import de.htwg.se.connectfour.mvc.controller._
import de.htwg.se.connectfour.mvc.model.types.CellType

import scala.io.StdIn
import scala.swing.Reactor

/**
 * Represents text user interface.
 * Whenever player plays turn,
 */
case class Tui(controller: Controller, gamingPlayers: GamingPlayers) extends Reactor with LazyLogging {

  listenTo(controller)
  reactions += {
    case _: PlayerGridChanged => showGridWithMessage()
    case _: GridChanged => showGrid()
    case _: PlayerWon => showWon()
    case _: Draw => showDraw()
    case _: FilledColumn => showGridWithMessage()
    case _: InvalidMove => showPlayAgain()
  }

  processInputLine("help")
  processInputLine("new 7 6")

  while (true) {
    do {
      processInputLine(StdIn.readLine())
    } while (!controller.gameFinished)
  }



  def processInputLine(input: String): Unit = {
    val parsedInput = input.split(" ")
    parsedInput(0) match {
      case "quit" => quit()
      case "new" => controller.actor ! EmptyGrid(7, 6, controller)
      case "undo" => controller.actor ! Undo(controller)
      case "redo" => controller.actor ! Redo(controller)
      case "show" => showGridWithMessage()
      case "help" => showHelp()
      case "save" => controller.actor ! Save(controller)
      case "load" => controller.actor ! Load(controller)
      case _ =>
        val col = toInt(parsedInput(0))
        lastCase(col)
    }
  }

  def showGridWithMessage(): Unit = {
    showGrid()
    showMessage()
  }

  def showMessage(): Unit = {
    println(controller.statusText)
    println("Player " + gamingPlayers.currentPlayer + " (" + gamingPlayers.currentPlayerCellType + ")"
      + " played turn.")
  }

  def showWon(): Unit = {
    println("Congratulations!")
    println("Player " + gamingPlayers.previousPlayer.name + " has won.")
    showFinished()
  }

  def showDraw(): Unit = {
    println("Draw, nobody won.")
    showFinished()
  }

  def showFinished(): Unit = {
    println("Game is finished.")
  }

  def showGrid(): Unit = println(controller.grid)

  def showPlayAgain(): Unit = {
    showGrid()
    println("Player " + gamingPlayers.currentPlayer + " (" + gamingPlayers.currentPlayerCellType + "), "
      + "please play again.")
  }

  def quit(): Unit = System.exit(0)

  def showHelp(): Unit = {
    println("Commands are:\n" +
      "help,\n" +
      "new,\n" +
      "quit,\n" +
      "undo,\n" +
      "redo,\n" +
      "show,\n" +
      "save,\n" +
      "load,\n" +
      "<num> for place draw")
  }

  def toInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case e: Exception => -1
    }
  }

  def lastCase(col: Int): Unit = {
    if (col == -1 || checkIfInputInRange(col)) {
      println("\n" +
        "please note the program's control for further information type 'help'" +
        "\n")
    } else {
      if (!controller.gameFinished) {
        if (Main.debug.filter) logger.info("Tui.processInputLine: !controller.gameFinished")
        controller.actor ! Move(col, gamingPlayers.currentPlayerCellType, controller);
      } else showFinished()
    }
  }

  def checkIfInputInRange(inputValue: Int): Boolean = {
    if ((0 to (controller.grid.columns - 1)).contains(inputValue)) false
    else true
  }
}

