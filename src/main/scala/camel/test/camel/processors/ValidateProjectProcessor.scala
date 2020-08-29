package camel.test.camel.processors

import org.apache.camel.component.netty.http.NettyHttpMessage
import org.apache.camel.{Exchange, Processor}

class ValidateProjectProcessor extends Processor {

  override def process(exchange: Exchange): Unit = {
    val message = exchange.getMessage(classOf[NettyHttpMessage])
    val body = message.getBody(classOf[String])

    println(s"Validating body: [$body]")

    if (isValidBody(body)) {
      println("Body is valid")
      exchange.setMessage(message)
    } else {
      println("Body is invalid")
      exchange.setException(new Exception("Body is not valid"))
    }
  }

  private def isValidBody(body: String): Boolean =
    body.length <= 5

}
