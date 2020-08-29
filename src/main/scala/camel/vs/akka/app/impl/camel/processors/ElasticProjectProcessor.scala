package camel.vs.akka.app.impl.camel.processors

import org.apache.camel.{Exchange, Processor}

import scala.concurrent.{ExecutionContext, Future}

class ElasticProjectProcessor(implicit ec: ExecutionContext) extends Processor {

  override def process(exchange: Exchange): Unit =
    Future {
      println("Creating project in ElasticSearch")
      Thread.sleep(5000)
      println("Project created in ElasticSearch")
    }

}
