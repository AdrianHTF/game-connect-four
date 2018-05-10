package de.htwg.se.connectfour.mvc.view

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.controller._

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
    while (!controller.gameFinished) {
      val currentPlayer = gamingPlayers.currentPlayer
      processInputLine(StdIn.readLine())
    }
    processInputLine(StdIn.readLine())
  }

  def processInputLine(input: String): Unit = {
    val parsedInput = input.split(" ")
    try {
      parsedInput(0) match {
        case "quit" => quit()
        case "new" => controller.createEmptyGrid(parsedInput.apply(1).toInt, parsedInput.apply(2).toInt)
        case "undo" => 1 to parsedInput(1).toInt foreach { _ => controller.undo() }
        case "redo" => 1 to parsedInput(1).toInt foreach { _ => controller.redo() }
        case "show" => showGridWithMessage()
        case "help" => println("Commands are: help, new <num> <num>, quit, undo <num>, redo <num>, show, <num> <num>, <num>")

        case _ => if (!controller.gameFinished) {
          logger.info("Tui.processInputLine: !controller.gameFinished")
          val col = parsedInput(0).toInt;
          controller.actor ! Move(col, gamingPlayers.currentPlayerCellType, controller);
        } else showFinished()
      }
    } catch {
      case e: Exception =>
        println("Unknown command")
        throw e
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

  def quit(): Nothing = sys.exit(0)
}
