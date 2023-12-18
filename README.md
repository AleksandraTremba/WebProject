# Project Okapi

## Purpose
The goal of the project is to make an easy-to-use platform where students can
track their time spent studying and completed tasks.

### Authors
* Maksim Usmanov (mausma)
* Aleksandra Tremba (altrem)
* Sten Smirnov (stsmir)

### Technologies Used

* Java 17+
* Spring Boot 3.1.3
* PostgreSQL
* MapStruct 1.5.5
* Liquibase 4.24.0 
* Lombok 1.18.30

## Setup
### Requirements
In order to get backend up and running, the system must have the following dependencies installed:
* OpenJDK 17
* Docker

### Building
Note: Internet connectivity is required to download dependencies. Without it
the source will not compile.

From the root of the source, run

`./mvnw clean install spring-boot:repackage`

After the task is completed, the `backend-SNAPSHOT` JAR file is created. In order to run it, open
from the `target` folder and run:

`java -jar  *.jar`

### Database Configuration

When in root folder, using Docker Compose run the following command:

`docker compose up -d`

Now, your backend should be up and running with PostgreSQL as the database.

## Features

**Customer**
* Registers a user and saves his data in the database
* Logs in the user, checks if the provided password is correct
* Updates user data including when entering the correct password
* Deletes a user
* When requesting /{username}, gets public data (e.g. username, description, etc)

**Tasks**
* Creates task with unique id and saves it in the database
* Tasks have a name, description and status
* Updates task with new name and description and saves the change in the database
* Updates the status of the task
* Deletes the task
* Has a search by title
* Has pagination
* Has sort by status


**Timer**
* Creates timer with unique id
* Is connected to the customer via customer id
* Starts timer and saves start time and end time in the database
* Stops timer, calculating the remaining time before the end time
* Updates the end time in database if the timer is continuing counting
* Resets timer

**Groups**
* Creates group with unique id
* Is connected to the customer via customer id
* Customers can be added to the group
* Customers can be deleted from the group
* Customer that created the group has an ADMIN role

**Records**
* Displays amount of customers registered, timers started, groups and tasks created
* Updates amount of users registered
* Updates amount of timers started
* Updates amount of groups and tasks created
