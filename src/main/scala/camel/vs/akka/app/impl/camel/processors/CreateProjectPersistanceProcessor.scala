package camel.vs.akka.app.impl.camel.processors

import camel.vs.akka.app.{Project, ProjectRepository}
import AsyncHttpProcessor.Result
import camel.vs.akka.app.impl.camel.processors.AsyncHttpProcessor.Result
import org.apache.camel.Exchange
import org.apache.camel.component.netty.http.NettyHttpMessage

import scala.concurrent.{ExecutionContext, Future}

class CreateProjectPersistanceProcessor(projectRepository: ProjectRepository)(implicit ec: ExecutionContext) extends AsyncHttpProcessor {

  override def processFuture(exchange: Exchange): Future[Result] = {
    val message = exchange.getMessage(classOf[NettyHttpMessage])
    val name = message.getBody(classOf[String])
    val project = Project(name)

    println(s"Creating project: $project")

    projectRepository.create(project)
      .map(project => Result(project.toString, 201))
  }

}
