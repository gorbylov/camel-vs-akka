package camel.test.akka.protocol

sealed trait ProcessingError

object ProcessingError {
  case class ValidationError(message: String) extends ProcessingError
}
