package camel.test.camel

import camel.test.camel.processors.{CreateProjectPersistanceProcessor, ElasticProjectProcessor, HealthCheckProcessor, ReadProjectPersistanceProcessor, ValidateProjectProcessor}
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode

class CustomRouteBuilder(
  createProjectServiceProcessor: CreateProjectPersistanceProcessor,
  readProjectServiceProcessor: ReadProjectPersistanceProcessor,
  validateProjectProcessor: ValidateProjectProcessor,
  elasticProjectProcessor: ElasticProjectProcessor,
  healthCheckProcessor: HealthCheckProcessor
) extends RouteBuilder {

  override def configure(): Unit = {
    restConfiguration
      .component("netty-http")
      .host("localhost")
      .port(9000)
      .bindingMode(RestBindingMode.auto)

    rest("/").get().to("direct:health-check")
    rest("/api/v1/projects/{name}").get().to("direct:read-project")
    rest("/api/v1/projects").post().to("direct:create-project")

    from("direct:health-check")
      .process(healthCheckProcessor)
    from("direct:read-project")
      .process(readProjectServiceProcessor)
    from("direct:create-project")
      .process(validateProjectProcessor)
      .process(createProjectServiceProcessor)
      .process(elasticProjectProcessor)

  }

}
