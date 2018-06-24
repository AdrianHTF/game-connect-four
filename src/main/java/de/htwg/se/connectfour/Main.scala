package de.htwg.se.connectfour

import akka.actor.{Actor, ActorSystem, Props}
import com.google.inject.Guice
import de.htwg.se.connectfour.mvc.controller.Controller
import de.htwg.se.connectfour.mvc.model.player.RealPlayer
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.persistence.{MongoDB, Slick_CellDB}
import de.htwg.se.connectfour.mvc.view.{GamingPlayers, Tui}

import scala.util.Random

object Main extends LazyLogging {

  object debug {
    def filter = false
  }

  class ServerActor extends Actor {
    def receive = {
      case s: String => println("String: " + s)
      case i: Int => println("Number: " + i)
    }
  }

  val system = ActorSystem("ActorSystem");
  val actor = system.actorOf(Props[ServerActor], "ServerActor")
  //val dbType = MongoDB
  val dbType = Slick_CellDB

  println()

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(new ConnectFourModule)
    val controller = injector.getInstance(classOf[Controller])
    controller.setActorSystem(system);
    val player1 = getPlayersName("Player 1")
    val player2 = getPlayersName("Player 2")
    val players = new GamingPlayers(player1, player2, controller, controller.actor)

    startGame(controller, players)
  }

  def getPlayersName(player: String): RealPlayer ={
    println(player + " Please insert Name\n")
    //val playername = StdIn.readLine().toString
    val playername = Random.alphanumeric.filter(_.isLetter).take(4).mkString
    if (playername != null) {
      val realPlayer = RealPlayer(playername)
      println(playername + " created\n")
      realPlayer
    }
    else
      getPlayersName(player)
  }

  def startGame(controller: Controller, players: GamingPlayers): Unit = {

    logger.info("starting")

    /*
    val cell = Cell(1,1, CellType.EMPTY)
    val mongo = MongoDB.create(cell)

    var i = 0
    for(i <- 0 to 1) {
      logger.info(s"DB Player $i: ${PlayerDB.read.apply(i)}")
    }
    */

    Tui(controller, players)

    /*
      Console.print("Do you want to start gui (y/n): ")
      val input = StdIn.readLine()
      if (input.equalsIgnoreCase("y")) {
        if (Main.debug.filter) logger.info("started GUI")
        Gui(controller, players)
      } else {
        if (Main.debug.filter) logger.info("started TUI")
        Tui(controller, players)
    }
    */
  }
}