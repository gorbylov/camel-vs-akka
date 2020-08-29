package camel.test.akka

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

trait FlowSupport {

  def compileFlow[In, Out, Mat](element: In)(flow: Flow[In, Out, Mat])(implicit actorSystem: ActorSystem): Future[Out] =
    Source.single(element)
      .via(flow)
      .runWith(Sink.head)

  def compileUnitFlow[Out, Mat](flow: Flow[Unit, Out, Mat])(implicit actorSystem: ActorSystem): Future[Out] =
    compileFlow(())(flow)

}
