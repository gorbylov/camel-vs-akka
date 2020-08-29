package camel.test.akka

import akka.actor.ActorSystem
import camel.app.Project
import camel.test.akka.flows._
import camel.test.akka.protocol.ProcessingError

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
