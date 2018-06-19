package de.htwg.se.connectfour.mvc.persistence

trait Dao[DATA, ID] {
  type T

  def create(data: DATA): T

  def read(): Seq[DATA]
}