
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

### Logging
- For local deployment, it is available on STDOUT using the format configured in [logback-spring.xml](src/main/resources/logback-spring.xml)

## Setup Guide
### Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

### **Here is our quickstart guide.**
* Clone the repo
```shell  
git clone git@github.com:AlexKimani/cards-api.git  
```  
* When you attempt to clone the repository, you receive the error message. [Fix – git@github.com : permission denied](https://dev.classmethod.jp/articles/fix-gitgithub-com-permission-denied-publickey-fatal-could-not-read-from-remote-repository/)
* [Install docker](https://docs.docker.com/get-docker/). Ensure that docker is always running.

## Running and testing
* via IDE for local debugging (recommended)
* Run all the required Services on Docker `docker-compose up`.  
  Please follow the troubleshooting section if you are facing any issue
* Start app via IDE (SpringBoot: `com.logicea.cardsapi.CardsApiApplication`). [Not Recommended]
* Build and Test
* `./mvnw clean package`
* Integration tests will use test configs
* Run
* via Docker
* `docker-compose build service-cards`.

### Available Service Endpoints



### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.3/maven-plugin/reference/html/#build-image)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.1.3/reference/html/features.html#features.testing.testcontainers)
* [Testcontainers R2DBC support Reference Guide](https://java.testcontainers.org/modules/databases/r2dbc/)
* [Testcontainers MySQL Module Reference Guide](https://java.testcontainers.org/modules/databases/mysql/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security.oauth2.client)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.reactive)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#data.sql.r2dbc)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.liquibase)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#actuator)
* [Prometheus](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#actuator.metrics.export.prometheus)
* [OAuth2 Authorization Server](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security.oauth2.authorization-server)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#web.security.oauth2.server)

## Service Maintainers
* Primary: Joe Alex Kimani
* GitHub: [alexkimani](https://github.com/AlexKimani)
* WakaTime: [@joealexkimani](https://wakatime.com/@joealexkimani)