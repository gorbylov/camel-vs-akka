package camel.vs.akka.app.impl.akka.http

import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import camel.vs.akka.app.Project
import camel.vs.akka.app.impl.akka.FlowBuilder

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait Routes {

  def routes(flowBuilder: FlowBuilder): Route =
    concat(
      get {
        pathEndOrSingleSlash {
          completeFuture {
            flowBuilder.healthCheck
          }
        }
      },
      post {
        path("api" / "v1" / "projects") {
          entity(as[String]) { name =>
            completeFuture {
              flowBuilder.createProject(Project(name))
            }
          }
        }
      },
      get {
        path("api" / "v1" / "projects" / Segment) { name =>
          completeFuture {
            flowBuilder.readProject(name)
          }
        }
      }
    )

  private def completeFuture[T](future: Future[T]) =
    onComplete(future) {
      case Success(value) => complete(value.toString)
      case Failure(ex)    => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
    }

}
