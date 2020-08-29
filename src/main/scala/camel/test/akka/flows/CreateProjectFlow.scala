package camel.test.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import camel.app.{Project, ProjectRepository}

import scala.concurrent.ExecutionContext

class CreateProjectFlow(projectRepository: ProjectRepository)(implicit ec: ExecutionContext) {

  val flow: Flow[Project, Project, NotUsed] =
    Flow[Project].mapAsync(1)(project => projectRepository.create(project))

}
