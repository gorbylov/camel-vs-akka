package camel.vs.akka.app

import scala.concurrent.Future

class ProjectRepositoryImpl extends ProjectRepository {

  private var map: Map[String, Project] = Map.empty

  override def create(project: Project): Future[Project] = {
    map = map + (project.name -> project)
    Future.successful(project)
  }

  override def read(name: String): Future[Option[Project]] =
    Future.successful(map.get(name))
}
