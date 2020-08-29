package camel.vs.akka.app.impl.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import camel.vs.akka.app.{Project, ProjectRepository}

import scala.concurrent.ExecutionContext

class CreateProjectFlow(projectRepository: ProjectRepository)(implicit ec: ExecutionContext) {

  val flow: Flow[Project, Project, NotUsed] =
    Flow[Project].mapAsync(1)(project => projectRepository.create(project))

}
