package camel.test.camel.processors

import camel.test.camel.processors.AsyncHttpProcessor.Result
import org.apache.camel.{AsyncCallback, Exchange, Message}
import org.apache.camel.support.{AsyncProcessorSupport, DefaultMessage, ExchangeHelper}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

abstract class AsyncHttpProcessor(implicit ec: ExecutionContext) extends AsyncProcessorSupport {

  def processFuture(exchange: Exchange): Future[Result]

  override def process(exchange: Exchange, callback: AsyncCallback): Boolean = {
    processFuture(exchange).onComplete {
      case Success(result)    => setResult(exchange, callback, result)
      case Failure(exception) => setResult(exchange, callback, internalError(exception))
    }

    false
  }

  def setResult(exchange: Exchange, callback: AsyncCallback, result: Result): Unit = {
    val old = exchange.getMessage

    // create a new message container so we do not drag specialized message objects along
    // but that is only needed if the old message is a specialized message
    val copyNeeded = !(old.getClass == classOf[DefaultMessage])

    if (copyNeeded) {
      val msg = new DefaultMessage(exchange)
      msg.copyFromWithNewBody(old, result.body)
      // replace message on exchange
      ExchangeHelper.replaceMessage(exchange, msg, false)
      msg.setHeader(Exchange.CONTENT_TYPE, "application/json")
      msg.setHeader(Exchange.HTTP_RESPONSE_CODE, result.httpCode)
    } else {
      // no copy needed so set replace value directly
      old.setBody(result.body)
      old.setHeader(Exchange.CONTENT_TYPE, "application/json")
      old.setHeader(Exchange.HTTP_RESPONSE_CODE, result.httpCode)
    }

    callback.done(false)
  }

  private def internalError(ex: Throwable): Result = Result(
    body = s"Internal error: $ex",
    httpCode = 500
  )

}

object AsyncHttpProcessor {
  case class Result(body: String, httpCode: Int)
}
