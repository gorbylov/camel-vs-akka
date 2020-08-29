package camel.vs.akka.app.impl.akka.flows

import akka.NotUsed
import akka.stream.scaladsl.Flow
import camel.vs.akka.app.Project

import scala.concurrent.{ExecutionContext, Future}

class ElasticProjectFlow(implicit ec: ExecutionContext) {

  val flow: Flow[Project, Project, NotUsed] =
    Flow[Project].map { project => asyncOp(project); project }

  def asyncOp(project: Project): Future[Unit] = Future {
    println(s"Creating project in ElasticSearch: $project")
    Thread.sleep(5000)
    println("Project created in ElasticSearch")
  }

}
