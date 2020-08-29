package camel.vs.akka.app.impl.akka.protocol

sealed trait ProcessingError

object ProcessingError {
  case class ValidationError(message: String) extends ProcessingError
}
