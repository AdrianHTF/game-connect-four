package de.htwg.se.connectfour

import java.util.Dictionary

import akka.actor.{Actor, ActorSystem, Props}
import com.google.inject.Guice
import de.htwg.se.connectfour.mvc.controller.Controller
import de.htwg.se.connectfour.mvc.model.player.{RandomBotPlayer, RealPlayer}
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.view.{GamingPlayers, Gui, HTTPServer, Tui}
import de.htwg.se.connectfour.mvc.persistence.MySlick
import de.htwg.se.connectfour._

import scala.io.StdIn

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

  println()

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(new ConnectFourModule)
    val controller = injector.getInstance(classOf[Controller])
    val player1 = RealPlayer(getPlayersName("Player 1"))
    val player2 = RealPlayer(getPlayersName("Player 2"))
    controller.setActorSystem(system);
    val players = new GamingPlayers(player1, player2, controller, controller.actor)

    startGame(controller, players)
  }

  def getPlayersName(player: String): String ={
    println(player + " Please insert Name\n")
    val playername = StdIn.readLine().toString
    if (playername != null)
      playername
    else 
      getPlayersName(player)
  }

  def startGame(controller: Controller, players: GamingPlayers): Unit = {

    logger.info("starting")


    val player = RealPlayer("Dave")
    val player1 = RealPlayer("asklödj")
    val x = MySlick.create(player)
    val y = MySlick.create(player1)
    println("db read 1: " + MySlick.read(x))
    println("db read 2: " + MySlick.read(y))
    //val db = Database.forConfig("database");

    //val webServer = new HTTPServer(controller, players, system)

    //logger.info("started " + webServer)



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
