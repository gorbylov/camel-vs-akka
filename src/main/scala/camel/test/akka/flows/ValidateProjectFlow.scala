package camel.test.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import camel.app.Project
import camel.test.akka.protocol.ProcessingError
import camel.test.akka.protocol.ProcessingError.ValidationError

object ValidateProjectFlow {

  def flowVia(onValid: Flow[Project, Project, NotUsed]): Flow[Project, Either[ProcessingError, Project], NotUsed] =
    Flow[Project].map(validate).flatMapConcat {
      case Right(project) => Source.single(project).via(onValid).map(Right.apply)
      case Left(error)  => Source.single(error).map(Left.apply)
    }

  private def validate(project: Project): Either[ValidationError, Project] =
    Either.cond(project.name.length <= 5, project, ValidationError("Invalid Project Name"))
}
