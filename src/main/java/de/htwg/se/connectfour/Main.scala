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
    val player1 = RealPlayer("Marek")
    val player2 = RandomBotPlayer(controller)
    controller.setActorSystem(system);
    val players = new GamingPlayers(player1, player2, controller, controller.actor)

    startGame(controller, players)
  }

  def startGame(controller: Controller, players: GamingPlayers): Unit = {

    logger.info("starting")

    val x = MySlick.create(RealPlayer("alkdf"))
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
