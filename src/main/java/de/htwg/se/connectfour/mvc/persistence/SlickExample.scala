package de.htwg.se.connectfour.mvc.persistence

import slick.jdbc.H2Profile.api._

import de.htwg.se.connectfour.mvc.model.player.RealPlayer

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object MySlick extends Dao[RealPlayer, Long] {

  def main(args: Array[String]): Unit = {
    val id = create(new RealPlayer("aababba"))
    val id2 = create(new RealPlayer("hans"))
    println(read(id))
    delete(id)
    println(read(id2))
    println(read(id))
  }

  val db = Database.forConfig("h2mem1")




  override def create(player: RealPlayer): Long = {
    println("create")
    12
  }

  override def read(id: Long): Try[RealPlayer] = {
    ???
  }

  override def delete(id: Long): Unit = {
    ???
  }
}