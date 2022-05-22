package performance

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import scala.concurrent.duration._

class PerfTest extends Simulation {

  val protocol = karateProtocol()

//csv feeder with circular strategy
  val csvFeeder = csv("csvData.csv").circular()

  val create = scenario("create and delete article").feed(csvFeeder).exec(karateFeature("classpath:com/performance/data/FeederDemo.feature"))

  setUp(
    create.inject(
    nothingFor(4), // 1
    atOnceUsers(1), // 2
    rampUsers(4).during(5), // 3
    // constantUsersPerSec(2).during(10), // 4
    // constantUsersPerSec(1).during(15).randomized, // 5
    // rampUsersPerSec(2).to(5).during(5), // 6
    // rampUsersPerSec(5).to(7).during(1.minutes).randomized,// 7
    //stressPeakUsers(1000).during(20) // 8
    ).protocols(protocol)
  )

}