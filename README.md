# Build the Backend System for a Car Website

This is Project #3 for the Udacity's [Full Stack Java Developer Nanodegree Program](https://www.udacity.com/course/java-programming-nanodegree--nd079), sponsored by Cognizant.
The goal of the project is to create an application that can communicate with other services and be able to be viewed and used through Swagger-based API documentation. This project incorporates skills with Spring Boot, APIs, documentation, and testing to implement a Vehicles API that serves as an endpoint to track vehicle inventory. While the primary Vehicles API will perform CRUD operations(opens in a new tab) (Create, Read, Update and Delete) related to vehicle details like make, model, color, etc., it will need to consume data from other APIs as well regarding location and pricing data. The project implements a RESTful API for the Vehicles API, as well as converting a Pricing Service API to a microservice.

You can download the P02-VehiclesAPI starter project code from the [Github repo](https://github.com/udacity/nd035-C2-Web-Services-and-APIs-Exercises-and-Project-Starter/tree/master/P02-VehiclesAPI).



## Instructions

Project repository where students implement a Vehicles API using Java and Spring Boot that can communicate with separate location and pricing services. The project dependencies require the use of Maven and Spring Boot, along with Java v11.

Check each component to see its details and instructions. Note that all three applications
should be running at once for full operation.

- [Vehicles API](##Vehicles API)
- [Pricing Service](##Pricing Service)
- [Boogle Maps](##Boogle Maps)

---

## Vehicles API

A REST API to maintain vehicle data and to provide a complete view of vehicle details including price and address.

### Features

- REST API exploring the main HTTP verbs and features - like GET, POST, PUT, PATCH, and DELETE to perform CRUD operations (Create, Read, Update, Delete) on resources
- Hateoas (Hypermedia as the Engine of Application State) - hypermedia link interactions
- Custom API Error handling using `ControllerAdvice`
- Swagger API docs
- HTTP WebClient
- MVC Test
- Automatic model mapping

### Instructions

#### TODOs

- Implement the `TODOs` within the `CarService.java` and `CarController.java`  files
- Add additional tests to the `CarControllerTest.java` file based on the `TODOs`
- Implement API documentation using Swagger

#### Run the Code

To properly run this application you need to start the Orders API and
the Service API first.

```
$ mvn clean package
```

```
$ java -jar target/vehicles-api-0.0.1-SNAPSHOT.jar
```

Import it in your favorite IDE as a Maven Project.

#### Operations

Swagger UI: http://localhost:8080/swagger-ui.html

#### Create a Vehicle

`POST` `/cars`
```json
{
   "condition":"USED",
   "details":{
      "body":"sedan",
      "model":"Impala",
      "manufacturer":{
         "code":101,
         "name":"Chevrolet"
      },
      "numberOfDoors":4,
      "fuelType":"Gasoline",
      "engine":"3.6L V6",
      "mileage":32280,
      "modelYear":2018,
      "productionYear":2018,
      "externalColor":"white"
   },
   "location":{
      "lat":40.73061,
      "lon":-73.935242
   }
}
```

#### Retrieve a Vehicle

`GET` `/cars/{id}`

This feature retrieves the Vehicle data from the database
and access the Pricing Service and Boogle Maps to enrich
the Vehicle information to be presented

#### Update a Vehicle

`PUT` `/cars/{id}`

```json
{
   "condition":"USED",
   "details":{
      "body":"sedan",
      "model":"Impala",
      "manufacturer":{
         "code":101,
         "name":"Chevrolet"
      },
      "numberOfDoors":4,
      "fuelType":"Gasoline",
      "engine":"3.6L V6",
      "mileage":32280,
      "modelYear":2018,
      "productionYear":2018,
      "externalColor":"white"
   },
   "location":{
      "lat":40.73061,
      "lon":-73.935242
   }
}
```

#### Delete a Vehicle

`DELETE` `/cars/{id}`

---

## Pricing Service

The Pricing Service is a REST WebService that simulates a backend that
would store and retrieve the price of a vehicle given a vehicle id as
input. In this project, you will convert it to a microservice.

### Features

- REST WebService integrated with Spring Boot

### Instructions

#### TODOs

- Convert the Pricing Service to be a microservice.
- Add an additional test to check whether the application appropriately generates a price for a given vehicle ID

#### Run the code

To run this service you execute:

```
$ mvn clean package
```

```
$ java -jar target/pricing-service-0.0.1-SNAPSHOT.jar
```

It can also be imported in your IDE as a Maven project.

---

## Boogle Maps

This is a Mock that simulates a Maps WebService where, given a latitude
longitude, will return a random address.

### Instructions

Via shell it can be started using

```
$ mvn clean package
```

```
$ java -jar target/boogle-maps-0.0.1-SNAPSHOT.jar
```

The service is available by default on port `9191`. You can check it on the
command line by using

```
$ curl http://localhost:9191/maps\?lat\=20.0\&lon\=30.0
``` 

You can also import it as a Maven project on your preferred IDE and
run the class `BoogleMapsApplication`.
