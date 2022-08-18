<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-spring-webflux.png" width="300" />
</p>

This `spring-security-reactive-pac4j-boot-demo` project is a Spring Security reactive boot demo using:
- Spring Security reactive + Spring Webflux + Spring Boot
- the [spring-webflux-pac4j](https://github.com/pac4j/spring-webflux-pac4j) security library
- the [spring-security-pac4j](https://github.com/pac4j/spring-security-pac4j) bridge from pac4j to Spring Security.

## Run and test

You can build the project and run it on [http://localhost:8080](http://localhost:8080) using the following commands:

    cd spring-security-reactive-pac4j-boot-demo
    mvn clean compile exec:java

or

    cd spring-security-reactive-pac4j-boot-demo
    mvn spring-boot:run
