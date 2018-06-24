package de.htwg.se.connectfour.mvc.persistence

import de.htwg.se.connectfour.mvc.controller.Controller

trait DaoMongo[DATA, ID] {
  type T

  def save(data: DATA): T

  def load(controller: Controller): T

}