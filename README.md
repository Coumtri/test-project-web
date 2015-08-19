Projet test web
=========================================

Web project source :
---------------------
https://github.com/kolorobot/spring-mvc-quickstart-archetype

Installation + war
-------------------

```bash
    git clone https://github.com/Coumtri/test-project-web.git
    mvn clean package
```

Run the project
----------------
mvn test tomcat7:run
OR
java -jar target/dependency/webapp-runner.jar target/*.war

Remote debugging from IDEA
---------------------------
mvnDebug clean tomcat7:run-war
source : http://stackoverflow.com/questions/22229088/intellij-idea-13-debugger-dont-stop-on-breakpoint-in-java-for-maven-project

Cloud execution
----------------
https://powerful-temple-3764.herokuapp.com/

Test on the browser
----------------------
http://localhost:8080/
https://powerful-temple-3764.herokuapp.com:8080/

Endpoint Integration
---------------------
Subscription Create Notification URL : https://powerful-temple-3764.herokuapp.com/subscription/create/notification?url={eventUrl}
Subscription Change Notification URL : https://powerful-temple-3764.herokuapp.com/subscription/change/notification?url={eventUrl}
Subscription Cancel Notification URL : https://powerful-temple-3764.herokuapp.com/subscription/cancel/notification?url={eventUrl}
Subscription Status Notification URL : https://powerful-temple-3764.herokuapp.com/subscription/status/notification?url={eventUrl}
Add-on Notification URL : https://powerful-temple-3764.herokuapp.com/subscription/create/notification?url={eventUrl}
User Assignment Notification URL : https://powerful-temple-3764.herokuapp.com/user/assignment/notification?url={eventUrl}
User Unassignment Notification URL : https://powerful-temple-3764.herokuapp.com/user/unassignment/notification?url={eventUrl}

