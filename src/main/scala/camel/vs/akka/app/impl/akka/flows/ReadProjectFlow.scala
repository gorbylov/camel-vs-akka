package camel.vs.akka.app.impl.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import camel.vs.akka.app.{Project, ProjectRepository}

class ReadProjectFlow(projectRepository: ProjectRepository) {

  val flow: Flow[String, Option[Project], NotUsed] =
    Flow[String].mapAsync(1)(name => projectRepository.read(name))

}
