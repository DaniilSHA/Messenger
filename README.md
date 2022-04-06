## Setup instructions

### Preparing

The following needs to be installed before building the project:

* JDK 8
* Maven 3.8.5
* Docker

### Run

Make all command in the flow step by step:

* in root folder in terminal: ./mvnw package
* in root folder in terminal: cp target/Messenger-0.0.1-SNAPSHOT.jar src/docker
* go in root/src/docker folder and in terminal: docker-compose up

### Stop

* in root/src/docker folder in terminal (another terminal, don't use terminal in previous step) : docker-compose down

## API instructions

* endpoint - POST http://localhost:8888/user/authentication
  with JSON body like:
  {
  "name" : "Наташа",
  "password" : "admin"
  } will return JSON with token like: {
  "token": "eyJhbGciOiJub25lIn0.eyJzdWIiOiLQndCw0YLQsNGI0LAifQ."
  }, which you should use in next step in headers
* endpoint - POST http://localhost:8888/message/send
  with JSON body like:
  {
  "name" : "Наташа",
  "message" : "some information" OR "history [N]" (N - amount of retrieve messages)
  } 
 and with headers like:
 KEY: Token
 VALUE: bearer_[token] (token - token which you receive in previous step). This endpoint store messages 
in the database or return the specified number of recent messages 