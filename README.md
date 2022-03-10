## Running the application locally

This is a Spring Boot application. You may directly execute the `main` method in the `com.sellics.casestudy.SearchRankingIndexApplication` class from your IDE.

You may also start application directly from your command line/terminal using maven

```shell
mvn spring-boot:run
```

Please note that application is using lombok, so you may need to do some config on your IDE. 
[Eclipse](https://projectlombok.org/setup/eclipse)

[IntelliJ](https://projectlombok.org/setup/intellij)


## Services
There are 3 end-points for each requirement in case study respectively in the project which may be called as below
- localhost:8081/api/v1/ranking/keyword-asin/f250/B092SS35LK - First path variable is keyword and second one is ASIN

- localhost:8081/api/v1/ranking/keyword/f250 - path variable is keyword

- localhost:8081/api/v1/ranking/asin/B092SS35LK - path variable is ASIN
