package de.htwg.se.connectfour.mvc.persistence

import scala.util.Try

trait Dao[DATA, ID] {
  def create(data: DATA): ID

  def read(id: ID): Try[DATA]

  def delete(id: ID)
}