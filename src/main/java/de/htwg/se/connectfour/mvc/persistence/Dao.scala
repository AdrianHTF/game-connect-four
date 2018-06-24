package de.htwg.se.connectfour.mvc.persistence

trait Dao[DATA, ID] {
  type T

  def save(data: DATA): T

  def load(): Seq[DATA]
}