package de.htwg.se.connectfour.mvc.persistence

import de.htwg.se.connectfour.mvc.controller.Controller

trait DaoMongo[DATA, ID] {
  type T

  def create(data: DATA): T

  def read(controller: Controller): T

}