package camel.test.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow

object HelthCheckFlow {

  val flow: Flow[Unit, String, NotUsed] =
    Flow[Unit].map(_ => "Hello World")

}
