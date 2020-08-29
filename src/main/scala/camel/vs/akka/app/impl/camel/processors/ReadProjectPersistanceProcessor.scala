package camel.vs.akka.app.impl.camel.processors

import camel.vs.akka.app.ProjectRepository
import AsyncHttpProcessor.Result
import org.apache.camel.Exchange
import org.apache.camel.component.netty.http.NettyHttpMessage

import scala.concurrent.{ExecutionContext, Future}

class ReadProjectPersistanceProcessor(projectRepository: ProjectRepository)(implicit ec: ExecutionContext) extends AsyncHttpProcessor {

  override def processFuture(exchange: Exchange): Future[AsyncHttpProcessor.Result] = {
    val message = exchange.getIn(classOf[NettyHttpMessage]) // <-- ClassCastException
    val name = message.getHeader("name", classOf[String]) // <-- ClassCastException

    projectRepository.read(name)
      .map {
        case Some(project) => Result(project.toString, 200)
        case None          => Result(s"Not Found: $name", 404)
      }
  }

}
