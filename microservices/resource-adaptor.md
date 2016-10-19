# Resource Adaptor

Resource Adaptor is a Proxy microservice that provides integration of 
heterogenous city's resources in the platform. A single Resource Adaptor must
be available on the Internet in order to properly expose its API.

Two important goals of the Smart City Platform are supported by 
the Resource Adaptor microservice:
  * Provide services to access and manipulate the resources of a city
  * Abstract implementation details of physical devices

> **What is a City Resource?**
> A city resource is literally a "Thing" in the world, if you think in Internet
> of Things terms. In practice, city's facilities, such as traffic lights, 
> buses, lampposts, and buildings, are few examples of *things* that may 
> be considered resources of a city. Resources may provide interaction
> capabilities to gather data and receive commands, mainly supported by
> sensors and actuators devices. A bus can report its location
> through a GPS sensor, while a traffic light may receive commands to change its
> state.

## Summary

* [Features and Services](#features-and-services)
* [Communication and Dependencies](#communication-and-dependencies)
* [Services to be integrated](#services-to-be-integrated)
* [Resource Meta-data](#resource-meta-data)
* [API](#api)
* [Examples](#examples)
* [Deployment](#deployment)
* [Roadmap](#roadmap)

## Features and Services

A Resource Adaptor aims to provide the following services:

* Provide services to integrate physical devices or external services via
standardized IoT and Web protocols. They
may use the following services:
  * Register in the platform as a resource
  * Send data to the platform (i.e.: sensor devices)
  * Subscribe to receive events and command notifications (i.e.: actuator devices)
* Intermediate all communication between the platform microservices and
city's resources.
  * Exposes a service API to provide resources' metadata and status
  * Exposes a standardized API to allow the [Data Collector]() microservice
  ask to the lastest data provided by resources
  * Exposes a standardized API to allow the [Actuator Controller]()
  microservice to send commands to resources with actuator capabilities

## Communication and Dependencies

Resource Adaptor uses the following microservice:

* [Resource Catalloguer]()
  * Register new resources in the platform
  * Update existing resources meta-data and operational status

## Resource Meta-data

In order to become accessible in the Smart City Platform, the Resource Adaptor
must register its resources in the Resource Cataloguer. Among other data,
each resource must send the basic URL to access its REST API. Let's consider 
the case when we have a Resource Adaptor encapsulating a sensors network from 
the University of São Paulo. Each sensor is a resource that must be registered
in the platform to become accessible. Suppose that the following resources URL
were registered in the Resource Cataloguer:

<br>

* Resource 1:
  * URL: http://localhost:3000/resources/1
  * lat: -23.55
  * lon: -46.73
  * collect_interval: 60
  * capabilities:
    * temperature
    * pressure
    * humidity
  * description: "Raspberry Pi"
* Resource 2:
  * URL: http://localhost:3000/basic_resources/1/components/2
  * lat: -23.56
  * lon: -46.30
  * collect_interval: 60
  * capabilities:
    * temperature
    * pressure
    * humidity
  * description: "Raspberry Pi"

<br>

Each of these URL will be considered as separate resources for the platform.
In addition to the URL, the location (latitude and longitude), 
collect interval in seconds, the capabilities and a description are informed
as well. Each resource will collect data every 60 seconds. Note that both 
resources are sensors, since they behave like sensors (respond to 
temperature, pressure, humidity). Instead of classifying each resource by type,
we chose to adopt [Duck Typing](https://pt.wikipedia.org/wiki/Duck_typing)
by determining the valid semantics of a resource through its methods. Thus, 
if a resource has the temperature capability, it is considered a resource
with temperature sensor. On the other hand, if the same resource also responds
to an actuator capability to manipulate a traffic light, it's also considered
a resource with an actuator.

## API

This API is provided as a REST API built on RESTful design principles.
This API uses resource-oriented URLs that are built over HTTP features, like
methods as verbs and response codes. All response bodies are JSON encoded, 
including error responses. This API is fully accessible by browser or any HTTP
common client, like curl.

### Authentication

This API does not support authentication yet.

### Requests

All resources managed by a Resource adaptor are accessed through the base URL:
> http://localhost:3000/resources

### HTTP Verbs

__TODO: FIX THIS__
Since all resources are read-only, this API only supports GET methods:

<br>

* GET - Retrieve a resource or a collection of resources

### Responses

<br>

* All responses bodies are JSON encoded.
* Timestamps are in UTC and formatted as ISO8601.
* Unset fields will be represented as a null instead of not being present. If the field is an array, it will be represented as an empty array: [].

### HTTP Status Code

We use HTTP status codes in each response.

<br>

* Success:

* 200 OK - Request succeeded.

* Error:

* 400 BadRequest - The request is badly formatted or the params is not passed correctly. Not implemented yet.
* 401 Unauthorized - No authentication credentials provided or authentication failed. Not implemented yet.
* 403 Forbidden - Authenticated user does not have access. Not implemented yet.
* 404 NotFound - Resource not found.
* 422 UnprocessableEntry - Could not process the entry due to validation errors.
* 429 TooManyRequests - Request rejected due to rate limiting. Not implemented yet.
* 500 InternalError - An internal server error occurred.

### Errors

You always should check the correct HTTP status. In some cases, we also return a JSON object with a message explaining the error. For example:

<br>

```
{
  "code": "NotFound",
    "message": "No such component"
}
```
<br>

### GET /components/

Return an array with all components objects encapsulated by a Resource Adaptor.

<br>

* **id:** long integer value of the internal id of component
* **uuid:** string value of the unique identifier provided by the Platform. If the uuid is null, that component is not registered yet.
* **lat:**  double value of the latitude position
* **lon:**  double value of the longitude positon
* **status:** string value with the current status of component
* **collect_interval:**   integer value of collect time interval in seconds
* **last_collection:**  hash with capabilities as key and the last collected data of that capability as value
* **capabilities:** list of capabilities comma separated
* **description:**  provided description of component
* **created_at:** timestamp of component creation. Should be in UTC and ISO8601 formatted.
* **updated_at:** timestamp of component last update. Should be in UTC and ISO8601 formatted.


#### Usage example:

<br>

`curl -H http://localhost:3000/basic_resources/1/components/`

<br>

```
[{
  "id": 1,
    "uuid": null,
    "lat": -23.559319,
    "lon": -46.731476,
    "status": "stopped",
    "collect_interval": 60,
    "last_collection": {
      "temperature": 294.69,
      "pressure": 934.47,
      "humidity": 45
    },
    "capabilities": ["temperature", "pressure", "humidity"],
    "description": "",
    "created_at": "2016-05-06T10:42:36.619Z",
    "updated_at": "2016-05-06T20:11:42.944Z"
}, {
  "id": 2,
    "uuid": null,
    "lat": -23.561528,
    "lon": -46.656009,
    "status": "stopped",
    "collect_interval": 60,
    "last_collection": {
      "temperature": 284.69,
      "pressure": 934.47,
      "humidity": 65
    },
    "capabilities": ["temperature", "pressure", "humidity"],
    "description": "Arduíno Uno",
    "created_at": "2016-05-06T10:42:36.727Z",
    "updated_at": "2016-05-06T20:11:13.244Z"
}]
```
<br>

### GET /components/status

Return an array with the status of each component encapsulated by a Resource Adaptor.

<br>

* **id:** long integer value of the internal id of component
* **status:** string value with the current status of component
* **updated_at:** timestamp of component last update. Should be in UTC and ISO8601 formatted.

#### Usage example:

<br>

`curl -H http://localhost:3000/basic_resources/1/components/status`

<br>

```
[{
  "id": 1,
    "status": "stoped",
    "updated_at": "2016-05-06T20:11:42.944Z"
},
{
  "id": 2,
  "status": "stoped",
  "updated_at": "2016-05-06T20:11:42.944Z",
}]
```

<br>

### GET /components/:id

Return a single component object encapsulated by a Resource Adaptor.

* **id:** long integer value of the internal id of component
* **uuid:** string value of the unique identifier provided by the Platform. If the uuid is null, that component is not registered yet.
* **lat:**  double value of the latitude position
* **lon:**  double value of the longitude positon
* **status:** string value with the current status of component
* **collect_interval:**   integer value of collect time interval in seconds
* **last_collection:**  hash with capabilities as key and the last collected data of that capability as value
* **capabilities:** list of capabilities comma separated
* **description:**  provided description of component
* **created_at:** timestamp of component creation. Should be in UTC and ISO8601 formatted.
* **updated_at:** timestamp of component last update. Should be in UTC and ISO8601 formatted.

<br>

#### Usage example:

<br>

`curl -H http://localhost:3000/basic_resources/1/components/1`

<br>

```
{
  "id": 1,
    "uuid": null,
    "lat": -23.559319,
    "lon": -46.731476,
    "status": "stopped",
    "collect_interval": 60,
    "last_collection": {
      "temperature": 294.69,
      "pressure": 934.47,
      "humidity": 45
    },
    "capabilities": ["temperature", "pressure", "humidity"],
    "description": "",
    "created_at": "2016-05-06T10:42:36.619Z",
    "updated_at": "2016-05-06T20:11:42.944Z"
}
```

<br>

### GET /components/:id/collect

Return a relation of each capability of a component and its collected value.

<br>

* **data:** hash with the relation between capabilities and collected values
* **updated_at:** timestamp of component last update. Should be in UTC and ISO8601 formatted.

<br>

#### Usage example:

<br>

`curl -H http://localhost:3000/basic_resources/1/components/1/collect`

<br>

```
{
  "data": {
    "temperature": 294.69,
      "pressure": 934.47,
      "humidity": 45
  },
    "updated_at": "2016-05-06T20:11:42.944Z"
}
```

<br>

### GET /components/:id/collect/:capability

Return the last collected data from the requested capability

<br>

* **data:** collect value from the required capability
* **updated_at:** timestamp of component last update. Should be in UTC and ISO8601 formatted.

<br>

#### Usage example:

<br>

`curl -H http://localhost:3000/basic_resources/1/components/1/collect/temperature`

<br>

```
{
  "data": 294.69,
    "updated_at": "2016-05-06T20:11:42.944Z"
}
```

## Examples

### Arduino - Your home as a city resource

> TODO

### External Web Service - São Paulo buses as a city resource

> TODO

## Deployment

> TODO

## Roadmap

--------------------------------------------------------------------

## Deprecated

### Integrate a Resource to the Platform

You can create your own Resource Adaptor! As long as it properly responds to the Resource Adaptors' documented REST API, no matter what is the specific implementation, programming language or how you manage your components. However, we have developed a Basic Framework that you can use to easily create new Resource Adaptors.

The Resource Adaptor Framework is an API only application built on Rails API gem which you can use to create new Resource Adaptors. Rails API is a subset of Rails which can be used by applications that don't require all functionalities that a complete Rails application provides.

### How to use the framework?

The first thing you need to do is clone the framework's repository. After this, read the README.md file and follow the defined steps to setup your development environment. Don't forget to run all tests to verify if the code is properly working.

To create sample components in your development environment, run:

<br>

`bundle exec rake component:create`

<br>

This task will create components in your local database using the data provided in `config/resource.yml` file. By now, don't worry about this.

After creating the sample data, you are allowed to run the Rails application and interact with the API that is documented in the previous section. So, run the application:

<br>

`rails s`

<br>


By default, the application will run in localhost and port 3000. Thus, you can interact with the API using a browser or the curl Linux utility as shown before. Note that, despite the main application being running, the components aren't collecting data yet. That's because we still need to start the data collection following these steps:

<br>

1. Open a new terminal
2. Run the collect script: rails runner scripts/collect.rb

<br>

The collect scripts create threads to collect data for each component. Besides that, it also offers a basic terminal interface which you can use to manage the existing componets. Type status to see status of all components. For more commands, type help. If you want to exit, use `ctrl+d`. This will stop the collection of all components.

<br>

## Programming your components

There are two main steps you must perform to create a new **Resource Adaptor** using our framework:

<br>

* Firstly, you must define what components your **Resource Adaptors** will encapsulate. You should think about what that component is (Micro-controller with many sensors, a whole hospital, a unique sensor, etc), what is the set of capabilities that component responds to (temperature, humidity, capacity, etc) and how they collect the data they provide (through an existing Web API, generating random data, etc). Once you have defined this, you must create a new ruby module to implement the
data collection of each capability of your component. This module should be included inside the module **ComponentServices** in the file `lib/component_services.rb`. This module is supposed to be the implementation of your new type of component. In this sense, you should create a name that appropriately represents the type of your components. It's also important to implement a method for each sensor capability of that type of component with the code to collect data. By convention, if your
new kind of component has the temperature and pressure capabilities, the related module should define the methods collect_temperature and collect_pressure which don't receive any params and return the collected value.
* Sencondly, you must define how to populate the database with all components' data you want to deploy. If your **Resource Adaptor** encapsulates 10 physical devices, you must have 10 components registered in the database with all their metadata, such as localization and capabilities. Fortunately, we provide an easy way to create new components: you can add all components' data into the `config/resource.yml` file. See the `README.md` of the repository to better understand how
to create components. Note that this method fails if you want to deploy many components or if you already have the necessary metadata in another external source, such as a CSV file or an API. In those cases, it'd be more interesting to create a script to register the components automatically in the database from existing data.

<br>

In order to better understand how the Framework works, take a look at the **Figure 1**:

<br>

![resource_adaptor_class_diagram](../images/resource_adaptor_class_diagram.png)

  **Figure 1** - Resource Adaptor Framework Models

  <br>

  As can be seen in Figure 1, a resource (BasicResource) has many components. The class Component is the main class of the Framework, where the Inversion of Control Principle is used. The method perform works as a template method with the common code to create a thread for a component and initialize the collection of data. On the other hand, the perform behavior is extended through inclusion of a specified module of each component, which implements the collect methods. Although this design
  works exactly as a Template Method Design Pattern, we use mixins and metaprogramming instead of basic inheritance mechanism.

  <br>

  Another important class of this diagram is the **ComponentsManager** which is a Singleton class used by the `collect.rb` script to manage the components collect life cycle.

