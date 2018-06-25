package de.htwg.se.connectfour.mvc.view

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.connectfour.mvc.controller.{Controller, Move, NewGrid}

class HTTPServer(controller: Controller, gamingPlayers: GamingPlayers, actorSystem: ActorSystem) extends LazyLogging {

  implicit val system = actorSystem //ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val route: Route = get {

    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Connect-Four</h1>"))
    }
    path("") {
      gridtoHtml
    } ~
      path("new") {
        controller.actor ! NewGrid(7, 6, controller)
        //controller.createEmptyGrid(7, 6)
        gridtoHtml
      } ~
      path("undo") {
        controller.undo
        gridtoHtml
      } ~
      path("redo") {
        controller.redo
        gridtoHtml
      } ~
      path(Segment) { command =>
        {
          processInputLine(command)
          gridtoHtml
        }
      }
  }


  def gridtoHtml: StandardRoute = {

    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Connect-Four</h1>" + controller.gridToHtml))
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  def unbind = {

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def processInputLine(input: String): Unit = {
    val column = input.toInt
    controller.actor ! Move(column, gamingPlayers.currentPlayerCellType, controller)
    //gamingPlayers.applyTurn(column)
  }
}
