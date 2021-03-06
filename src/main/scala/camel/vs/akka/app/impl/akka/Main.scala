package camel.vs.akka.app.impl.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import camel.vs.akka.app.ProjectRepositoryImpl
import camel.vs.akka.app.test.akka.flows._
import camel.vs.akka.app.impl.akka.flows.{CreateProjectFlow, ElasticProjectFlow, ReadProjectFlow}
import camel.vs.akka.app.impl.akka.http.Routes

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App with Routes {

  implicit val actorSystem: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  val projectRepository = new ProjectRepositoryImpl
  val createProjectPersistanceFlow = new CreateProjectFlow(projectRepository)
  val readProjectPersistanceFlow = new ReadProjectFlow(projectRepository)
  val elasticProjectFlow = new ElasticProjectFlow
  val customFlowBuilder = new FlowBuilder(
    createProjectPersistanceFlow,
    readProjectPersistanceFlow,
    elasticProjectFlow
  )

  val httpRoutes = routes(customFlowBuilder)
  val bindingFuture = Http().newServerAt("localhost", 9000).bind(httpRoutes)


  println(s"Server online at http://localhost:9000/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())
}
