# Java Demo Assignment

## Overview
The following repo contains the demo to bank accounts.

## Guidelines
Run the demo to create bank account and make transactions. User are already created for this example

### Prerequisites
1. Java (18)
2. Docker

#### Start locally
1. Start docker
2. Create postgres database in docker.
    - This command create a database in docker container. `docker run --name postgres-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:14.1-alpine`

3. Clone this repo `git@github.com:lucasscarlatta/demo-customer-api.git`
4. Go to project path.
   - `cd /path/to/project`
4. Build application 
    - This command create the jar file `./gradle clean build`
    
5. Run the app
    - `cd build/libs && java -jar demo-0.0.1-SNAPSHOT.jar`

#### Running with docker compose
1. Execute `docker-compose up -d`

### Call endpoints using curl
1. Check health, server should be "UP"
   - `curl -X GET -H "Content-Type: application/json" \
     http://localhost:8080/actuator/health`
2. List all users
   - `curl -X GET -H "Content-Type: application/json" \
     http://localhost:8080/v1/users`
     
3. Create account associated to customerId
   - Execute `curl -X POST -H "Content-Type: application/json" \
     -d '{"name": "AccountName", "customerId": "${customerId}"}' \
     http://localhost:8080/v1/accounts`
   - Replace `${customerId}` by a real customerId generated

4. List all accounts
   - `curl -X GET -H "Content-Type: application/json" \
     http://localhost:8080/v1/accounts`

5. Find account by id
   - `curl -X GET -H "Content-Type: application/json" \
     http://localhost:8080/v1/accounts/${id}`
   - Replace `${id}` by a real account id

6. Find accounts by customerId
   - `curl -X GET -H "Content-Type: application/json" \
     http://localhost:8080/v1/accounts/customers/${customerId}`
   - Replace `${customerId}` by a real customerId generated
   
#### Use openApi to call endpoint
1. Go to [SwaggerUI](http://localhost:8080/swagger-ui.html)
2. Call the different endpoints

#### Run test
- Execute unit test `./gradle clean test`
- Execute integration test `./gradle clean integrationTest`
- Execute all test `./gradle clean check`
