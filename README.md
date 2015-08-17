Projet test web
=========================================

Web project source :
---------------------
https://github.com/kolorobot/spring-mvc-quickstart-archetype

Installation
------------

```bash
    git clone https://github.com/Coumtri/test-project-web.git
    mvn clean package
```

Run the project
----------------
mvn test tomcat7:run
OR 
java -jar target/dependency/webapp-runner.jar target/*.war

Remote debugging
-----------------
mvnDebug clean tomcat7:run-war

source : http://stackoverflow.com/questions/22229088/intellij-idea-13-debugger-dont-stop-on-breakpoint-in-java-for-maven-project

Cloud execution
----------------
https://powerful-temple-3764.herokuapp.com/

Test on the browser
--------------------
http://localhost:8080/