package de.htwg.se.connectfour.mvc.persistence

import com.google.inject.Guice
import com.mongodb.casbah.MongoConnection
import de.htwg.se.connectfour.ConnectFourModule
import de.htwg.se.connectfour.mvc.controller.Controller
import de.htwg.se.connectfour.mvc.model.Cell
import de.htwg.se.connectfour.mvc.model.types.CellType
import org.specs2.mutable.Specification

class MongoDBTest extends Specification {

  "A Mongo DB" should {

    val mongoConn = MongoConnection()
    val mongoColl = mongoConn("casbah_test")("test_data")

    val cell = Cell(1, 1, CellType.FIRST)

    val injector = Guice.createInjector(new ConnectFourModule)
    val controller = injector.getInstance(classOf[Controller])

    "be able to save data" in {
      val saved = MongoDB.create(cell)
      ok
    }

    "be able to load data" in {
      MongoDB.read(controller)

      cell must be_==(controller.grid.cell(1,1))
    }

  }
}

