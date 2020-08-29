package camel.vs.akka.app.impl.akka

import akka.actor.ActorSystem
import camel.vs.akka.app.Project
import camel.vs.akka.app.test.akka.flows._
import camel.vs.akka.app.impl.akka.flows.{CreateProjectFlow, ElasticProjectFlow, HelthCheckFlow, ReadProjectFlow, ValidateProjectFlow}
import camel.vs.akka.app.impl.akka.protocol.ProcessingError

import scala.concurrent.{ExecutionContext, Future}

class FlowBuilder(
  createProjectServiceFlow: CreateProjectFlow,
  readProjectFlow: ReadProjectFlow,
  elasticProjectFlow: ElasticProjectFlow
)(implicit actorSystem: ActorSystem, ec: ExecutionContext) extends FlowSupport {


  def healthCheck: Future[String] = compileUnitFlow {
    HelthCheckFlow.flow
  }

  def readProject(name: String): Future[Option[Project]] = compileFlow(name) {
    readProjectFlow.flow
  }

  def createProject(project: Project): Future[Either[ProcessingError, Project]] = compileFlow(project) {
    ValidateProjectFlow.flowVia(
      createProjectServiceFlow.flow via elasticProjectFlow.flow
    )

    // Graphs alternative
    // source ~> validateFlow ~> either.in
    //                           either.left  ~> errorHandlingFlow                          ~> merge
    //                           either.right ~> createProjectFlow ~> elasticProjectFlow ~> ~> merge ~> resultSink
  }

}
