
# Cards Service
Assume an application named Cards that allows users to create and manage tasks in the form of cards:
- Application users are identified uniquely by their mail address, have
  a role (Member or Admin) and use a password to authenticate
  themselves before accessing cards
  * Members have access to cards they created
  * Admins have access to all cards
- A user creates a card by providing a name for it and, optionally, a description and a color
  * Name is mandatory
  * Color, if provided, should conform to a “6 alphanumeric characters prefixed with a #“ format
  * Upon creation, the status of a card is To Do
- A user can search through cards they have access to:
  - Filters include name, color, status and date of creation
  - Optionally limit results using page & size or offset & limit options
  - Results may be sorted by name, color, status, date of creation
- A user can request a single card they have access to,
- A user can update the name, the description, the color and/or the status of a card they have access to
  - Contents of the description and color fields can be cleared out
  - Available statuses are To Do, In Progress and Done
- A user can delete a card they have access to

### Build
**To run and test the application refer to:** [BUILD.md](data%2FBUILD.md)
 

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.3/maven-plugin/reference/html/#build-image)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.1.3/reference/html/features.html#features.testing.testcontainers)
* [Testcontainers R2DBC support Reference Guide](https://java.testcontainers.org/modules/databases/r2dbc/)
* [Testcontainers MySQL Module Reference Guide](https://java.testcontainers.org/modules/databases/mysql/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.reactive)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql.r2dbc)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.liquibase)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#actuator)
* [Prometheus](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#actuator.metrics.export.prometheus)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security)

## Service Maintainers
* Primary: Joe Alex Kimani
* GitHub: [alexkimani](https://github.com/AlexKimani)
* WakaTime: [@joealexkimani](https://wakatime.com/@joealexkimani)