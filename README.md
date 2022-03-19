# Backend Engineering Challenge
The original document can be found there: [the original doc](docs/assignement.md).

## Technical and design notes regarding this solution
Let's start from practical aspects:
### How to ...
#### ... build
```shell
./gradlew build -x test
```
#### ... run tests
```shell
./gradlew test
```
#### ... run with in-memory (not so interesting)
```shell
./gradlew bootRun
```
will be available on http://localhost:8080/
#### ... reach OpenAPI specification
Run locally any way and access http://localhost:8080/swagger-ui/index.html
#### ... run with PostgreSQL (more fun)
NB: It assumes that you have docker and docker-compose installed and running on your workstation.

Initialize and start docker by:
```shell
cd postgres
docker-compose build
docker-compose up
```
Run app in `prod` mode
```shell
cd ..
./gradlew bootRun --args='--spring.profiles.active=prod'
```
The app will be available on http://localhost:8080/.

The PostgresSQL will be available on 5432 port (user: `postgres`, db: `postgres`, password: `mysecretpassword`)
#### ... how to populate PostgreSQL DB by data (around 10 millions)
NB:  it could take some time
```shell
cd postgres
# if datafile is still gzipped
gzip -d insert.sql.gz
# copy it inside docker
docker cp insert.sql postgres_postgres_1:/insert.sql
# perform batch insert
docker exec -it postgres_postgres_1 psql --username postgres --dbname postgres -f /insert.sql > /dev/null
```

### Design notes
**NB**: The application was left as Spring MVC app for simplicity’s sake, 
however for apps under high load it's better to use reactive architecture (WebFlux by Spring, Project Reactor, VertX ....) (it's
increase throughput :) , but also increase latency :( ).

#### Separate models for storage and representation
Here I use different models for storage and request/response encodings since the way we store our entities shouldn't 
affect its representation. For mapping back and forth I use [Mapstruct library](https://mapstruct.org/). 

#### The lack of business entities
Some services with complex business logic have 3rd type of entities - "domain entities" (like those from DDD) and the 
flow inside looks like this (when we store something): `representation entities -> business entities (for domain logic) 
 -> db entities`. In this app there is no such complex logic, so business entities aren't used.

#### Reference data in memory
Some reference data like the types of orders, types of artists, store locations we usually
keep in memory cached for quick access. To keep them in actual state we could
listen to a stream/web socket about updates or (in the simplest case) update periodically.
However, in this case it was used as a part of schema, so was left them fixed in code. 

#### Marking that a response isn't complete
Since it was said in the original description that the pagination isn't 
required I added a mark in the response when we search records, that probably there is some extra data in DB
and search criteria should be refined.

#### OpenAPI specification
To make it possible to other developers to play with the service 
and be able to understand API - the OpenAPI specification is 
available [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) when the service is running.

### Technical notes
#### The usage of pg_trgm module
Since only limited amount of databases supports non-left-anchored string search
requests (something like `LIKE %blah-blah`) I use [pg_trgm](https://www.postgresql.org/docs/current/pgtrgm.html) module 
together with GIN index for textual fields. 

#### Custom Error Handler
Implemented in [CustomExceptionHandler](src/main/java/com/rize/test/controller/CustomExceptionHandler.java)

####
If the DB is expected to be shared resource  a Circuit Breaker pattern should be added,
but I this it's a little bit out of scope for this assignment.