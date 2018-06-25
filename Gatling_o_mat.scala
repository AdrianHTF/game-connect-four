/**
 * Copyright 2011-2018 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class Gatling_o_mat extends Simulation {

  val scenarioName = "Scenario Name"
  var requestIndex = 0;

  val httpConf = http
    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  object Actions {

    def move(column: Int) = exec(
      http("Move " + column).get("/computers/" + column)
    )

    def sleep(duration: Int, dura: Int) = pause(duration.milliseconds, dura.milliseconds)
	
	def newGame() = exec(      
	  http("New game").get("/computers/new")
    )
  }

  var scn = scenario(scenarioName)
	.exec(Actions.sleep(1000, 2000))
	  .repeat(1) {
	    exec(Actions.newGame)
	  }
      .exec(Actions.sleep(1000, 2000))
      .repeat(3) {
		exec(Actions.sleep(600, 1000))
        exec(Actions.move(5))
      }
      .repeat(3) {
		exec(Actions.sleep(600, 1000))
        exec(Actions.move(1))
      }
      .repeat(3) {
		exec(Actions.sleep(600, 1000))
        exec(Actions.move(2))
      }
      .repeat(3) {
		exec(Actions.sleep(600, 1000))
        exec(Actions.move(3))
      }
      .repeat(3) {
		exec(Actions.sleep(600, 1000))
        exec(Actions.move(4))
      }
	  .exec(Actions.sleep(1000, 1000))

  setUp(scn.inject(atOnceUsers(1500)).protocols(httpConf))
}
