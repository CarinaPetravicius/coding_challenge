# coding_challenge
A Coding Challenge with Java 17, Maven, Spring Boot, Clean Arch and Postgres.

## Starting the project

You must have the openjdk 17, maven, docker and docker compose.

1. Check the java version:

       java -version

2. In the root path of the project, start the docker compose to get it up the Database Postgres:

       docker-compose up

3. Set up the local environment variables in your Idea:

       POSTGRES_URL=jdbc:postgresql://localhost:5432/db
       POSTGRES_USERNAME=postgres
       POSTGRES_PASSWORD=postgres

4. For build the application and run the unit tests:

       mvn clean install

5. Run the main java class CodingChallengeApplication.