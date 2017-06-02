package de.htwg.se.connectfour.view

import java.awt.Color

import de.htwg.se.connectfour.controller.GridController
import de.htwg.se.connectfour.model.CellType
import de.htwg.se.connectfour.model.player.GamingPlayers

import scala.swing._
import scala.swing.event.Key

class Gui(val gridController: GridController, val gamingPlayers: GamingPlayers) {

  val rows: Int = gridController.columnSize
  // row = columnSize = 7
  val columns: Int = gridController.rowSize // TODO: resolve mixed row and column

  var slots: Array[Array[Label]] = Array.ofDim[Label](rows, columns)
  val buttons: Array[Button] = new Array[Button](rows)

  var mainFrame = new MainFrame()

  setup()

  def setup(): Unit = {
    setupButtons()
    setupSlots()
    setupMainFrame()
  }

  def setupButtons(): Unit = {
    for (i <- 0 until rows) {
      val chosenColumn: Int = i
      buttons(i) = Button(String.valueOf(i + 1))(buttonAction(chosenColumn))
    }
  }

  def buttonAction(chosenColumn: Int): Unit = {
    evaluateMove(chosenColumn)
    if (!gamingPlayers.currentPlayer.isReal) playBot()
  }

  def playBot(): Unit = {
    val robotsColumn = gamingPlayers.currentPlayer.playTurn()
    evaluateMove(robotsColumn)
  }

  def setupSlots(): Unit = {
    for (column <- 0 until columns; row <- 0 until rows) {
      slots(row)(column) = new Label {
        opaque = true
        horizontalAlignment
        border = Swing.LineBorder(Color.BLACK, 1)
      }
    }
  }

  def setupMainFrame(): Unit = {
    val WIDTH = 700
    val HEIGHT = 500

    mainFrame.title = "Connect four game"
    mainFrame.preferredSize = new Dimension(WIDTH, HEIGHT)
    mainFrame.contents = createPanel()
    mainFrame.menuBar = createMenuBar()
    mainFrame.visible = true
    mainFrame.centerOnScreen()
  }

  def createPanel(): GridPanel = {
    new GridPanel(rows, columns + 1) {
      for (i <- 0 until rows) {
        contents += buttons(i)
      }
      for (column <- 0 until columns - 1; i <- 0 until rows) {
        contents += slots(i)(column)
      }
    }
  }

  def createMenuBar(): MenuBar = {
    new MenuBar {
      contents += new Menu("Game") {
        mnemonic = Key.G
        contents += new MenuItem(Action("New") {
          startNewGame()
        })
        contents += new MenuItem(Action("Quit") {
          mnemonic = Key.Q
          quit()
        })
      }
      contents += new MenuItem(Action("Undo") {
        gridController.undo()
        gamingPlayers.changePlayer() // TODO: don't change player when grid is empty
        updateBoard()
      })
      contents += new MenuItem(Action("Redo") {
        gridController.redo()
        gamingPlayers.changePlayer() // TODO: don't change player when grid is empty
        updateBoard()
      })
    }
  }


  def updateBoard(): Unit = {
    for (row <- 0 until rows; column <- 0 until columns) {
      updateCell(row, column)
    }
  }

  def evaluateMove(chosenColumn: Int): Unit = {

    val columnFull: Boolean = gridController.isFullAndAddCell(chosenColumn, gamingPlayers.currentPlayerCellType())
    updateBoard()

    if (!columnFull) {
      if (gridController.isMoveWinning(chosenColumn)) showWon()
      else if (gridController.isFull) showDraw()

      gamingPlayers.changePlayer()
      mainFrame.title = "Connect four - player " + gamingPlayers.currentPlayer.name
    } else {
      Dialog.showMessage(null, "Please choose another one.", "Column is filled")
    }
  }

  def updateCell(row: Int, column: Int): Unit = {
    gridController.cell(row, column).cellType match {
      case CellType.FIRST =>
        slots(row)(column).background = Color.red
      case CellType.SECOND =>
        slots(row)(column).background = Color.blue
      case CellType.EMPTY =>
        slots(row)(column).background = Color.white
    }
  }


  def showWon(): Unit = {
    val winner: String = "Player " + gamingPlayers.currentPlayer.name + " has won"
    val option = Dialog.showConfirmation(null, "Play a new game?", optionType = Dialog.Options.YesNo, title = winner)
    startNewOrQuit(option == Dialog.Result.Ok)
  }

  def showDraw(): Unit = {
    val option = Dialog.showConfirmation(null, "Nobody won.\nPlay a new game?", optionType = Dialog.Options.YesNo, title = "Draw")
    startNewOrQuit(option == Dialog.Result.Ok)
  }

  private def startNewOrQuit(startNew: Boolean) = {
    if (startNew) {
      startNewGame()
    } else {
      quit()
    }
  }

  def startNewGame(): Unit = {
    gridController.createEmptyGrid(gridController.columnSize, gridController.rowSize)
    updateBoard()
  }

  def quit(): Unit = {
    mainFrame.dispose()
    sys.exit(0)
  }


}
