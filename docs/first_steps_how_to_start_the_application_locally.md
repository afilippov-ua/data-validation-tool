# How to start the application locally

##### Before starting the application locally you have to:
- override the next mongoDB properties in the `application.properties` file: 
```
spring.data.mongodb.host=<mongo_host>
spring.data.mongodb.port=<mongo_port>
spring.data.mongodb.database=<mongo_database_name>
```

##### To start application locally run the next command:
- for unix-like machines: `./gradlew bootRun`
- for windows-like machines: `gradlew.bat bootRun`

##### API base path is:
[http://localhost:8090](http://localhost:8090)

##### Swagger is available by the next path:
[http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)