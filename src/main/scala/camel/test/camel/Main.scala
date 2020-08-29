package camel.test.camel

import camel.app.{ProjectRepository, ProjectRepositoryImpl}
import camel.test.camel.processors.{CreateProjectPersistanceProcessor, ElasticProjectProcessor, HealthCheckProcessor, ReadProjectPersistanceProcessor, ValidateProjectProcessor}
import org.apache.camel.impl.DefaultCamelContext

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Main extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  val projectRepository: ProjectRepository = new ProjectRepositoryImpl

  val healthCheckProcessor = new HealthCheckProcessor
  val readProjectServiceProcessor = new ReadProjectPersistanceProcessor(projectRepository)
  val createProjectServiceProcessor = new CreateProjectPersistanceProcessor(projectRepository)
  val validateProjectProcessor = new ValidateProjectProcessor
  val elasticProjectProcessor = new ElasticProjectProcessor

  val routeBuilder = new CustomRouteBuilder(
    createProjectServiceProcessor,
    readProjectServiceProcessor,
    validateProjectProcessor,
    elasticProjectProcessor,
    healthCheckProcessor
  )

  val context = new DefaultCamelContext()

  context.addRoutes(routeBuilder)

  context.start()

  println(s"Server online at http://localhost:9000/\nPress RETURN to stop...")
  StdIn.readLine()

  context.stop()
}
