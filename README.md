# Cards Service
Assume an application named Cards that allows users to create and manage tasks in the form of cards:

- Application users are identified uniquely by their mail address, have a role (Member or Admin) and use a password to authenticate
themselves before accessing cards

    - Members have access to cards they created
  - Admins have access to all cards
- A user creates a card by providing a name for it and, optionally, a description and a color
  - Name is mandatory
  - Color, if provided, should conform to a “6 alphanumeric characters prefixed with a #“ format
  - Upon creation, the status of a card is To Do
- A user can search through cards they have access to
  - Filters include name, color, status and date of creation
  - Optionally limit results using page & size or offset & limit options
  - Results may be sorted by name, color, status, date of creation
- A user can request a single card they have access to
- A user can update the name, the description, the color and/or the status of a card they have access to
  - Contents of the description and color fields can be cleared out
  - Available statuses are To Do, In Progress and Done
- A user can delete a card they have access to
### Logging
- For local deployment, it is available on STDOUT using the format configured in [log4j2.yaml](src/main/resources/log4j2.yaml)

## Setup Guide
### Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)

### **Here is our quickstart guide.**
* Clone the repo
```shell
 git clone git@github.com:LeapaHQ/property-management-module.git
```
* When you attempt to clone the repository, you receive the error message. [Fix – git@github.com : permission denied](https://dev.classmethod.jp/articles/fix-gitgithub-com-permission-denied-publickey-fatal-could-not-read-from-remote-repository/)
* [Install docker](https://docs.docker.com/get-docker/). Ensure that docker is always running.

## Running and testing
* via IDE for local debugging (recommended)
    * Run all the required Services on Docker `docker-compose up`.
      Please follow the troubleshooting section if you are facing any issue
    * Start app via IDE (SpringBoot: `com.leapa.property.PropertyManagementApplication`). [Not Recommended]
* Build and Test
    * `./mvnw clean package`
    * Integration tests will use test configs
* Run
    * via Docker
        * `docker-compose build service-property-management`.

### Available Service Endpoints


### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.2/reference/htmlsinge/index.html#web.reactive)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.1.2/reference/htmlsinge/index.html#appendix.configuration-metadata.annotation-processor)
* [GCP Storage](https://googlecloudplatform.github.io/spring-cloud-gcp/reference/html/index.html#cloud-storage)
* [Config Client Quick Start](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_client_side_usage)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [GCP Storage](https://github.com/GoogleCloudPlatform/spring-cloud-gcp/tree/main/spring-cloud-gcp-samples/spring-cloud-gcp-storage-resource-sample)

## Service Maintainers
* Primary: Joe Alex Kimani
    * GitHub: [alexkimani](https://github.com/AlexKimani)
    * Slack: [@Joe](https://app.slack.com/team/U05K5FA2G75)
