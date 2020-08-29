package camel.test.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import camel.app.{Project, ProjectRepository}

class ReadProjectFlow(projectRepository: ProjectRepository) {

  val flow: Flow[String, Option[Project], NotUsed] =
    Flow[String].mapAsync(1)(name => projectRepository.read(name))

}
