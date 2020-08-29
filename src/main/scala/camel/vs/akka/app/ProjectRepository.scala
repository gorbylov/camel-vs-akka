package camel.vs.akka.app

import scala.concurrent.Future

trait ProjectRepository {

  def create(project: Project): Future[Project]

  def read(name: String): Future[Option[Project]]

}
