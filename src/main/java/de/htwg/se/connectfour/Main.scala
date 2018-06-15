package de.htwg.se.connectfour

import akka.actor.{Actor, ActorSystem, Props}
import com.google.inject.Guice
import de.htwg.se.connectfour.mvc.controller.Controller
import de.htwg.se.connectfour.mvc.model.player.{RandomBotPlayer, RealPlayer}
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.view.{GamingPlayers, Gui, HTTPServer, Tui}
import de.htwg.se.connectfour.mvc.persistence.PlayerDB

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

  var http : Boolean = false

  val system = ActorSystem("ActorSystem");
  val actor = system.actorOf(Props[ServerActor], "ServerActor")

  println()

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(new ConnectFourModule)
    val controller = injector.getInstance(classOf[Controller])
    controller.setActorSystem(system);
    val player1 = getPlayersName("Player 1")

    // Evtl Noetig für Akka-Http
    //val player2 = RandomBotPlayer(controller)

    val player2 = getPlayersName("Player 2")


    val players = new GamingPlayers(player1, player2, controller, controller.actor)

    startGame(controller, players)
  }

  def getPlayersName(player: String): RealPlayer ={
    println(player + " Please insert Name\n")
    val playername = StdIn.readLine().toString
    if (playername != null) {
      val realPlayer = RealPlayer(playername)
      PlayerDB.create(realPlayer)
      realPlayer
    }
    else
      getPlayersName(player)
  }

  def startGame(controller: Controller, players: GamingPlayers): Unit = {

    logger.info("starting")

    var iterate : Int = 2

    //Noetig für Http
    http = true

    for(i <- 1 to iterate) {
      logger.info(s"DB Player $i: ${PlayerDB.read(i)}")
    }

    if (http) {
      val webServer = new HTTPServer(controller, players, system)
    }
    else
      Tui(controller, players)
  }
}
