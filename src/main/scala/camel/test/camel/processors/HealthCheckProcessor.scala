package camel.test.camel.processors

import org.apache.camel.support.DefaultMessage
import org.apache.camel.{Exchange, Processor}

class HealthCheckProcessor extends Processor {

  override def process(exchange: Exchange): Unit = {
    val message = new DefaultMessage(exchange)
    message.setBody("Hello World")
    exchange.setMessage(message)
  }

}
