#Back-end side of movie-library-assignement application

This project contains a back-end Spring Boot server retrieving data from a moviesLib.json file using REST Api services exposed at http://localhost:8080/moviesLibrary. The Api controller contains view, creation, update, and delete services.
To manage data easily, an index field is generated automatically and added for objects.
A creation service is available to create and add a new element to existing objects.
It is possible de view all data or to use an index to search one object.
There is also another service, based on a key-value search criteria.
In addition, a delete and update operations are available to mdoify objects list content.
An integrity check is added before every process to control input parameters.

##Application Testing

The sources have been pre-compiled and can be run by going under the target/ directory and executing java -jar moviesLibrary-0.0.1-SNAPSHOT.jar command. In order to recompile it, execute mvn package -f "./pom.xml" under root directory. Java 8 and above is required for this.

A junit class, which contains 13 test cases, was used to validate functionalities.
In addition, a swagger configuration class was added to use Swagger ui tool in testing. Once the application is started, services can be tested via http://localhost:8080/swagger-ui.html.
