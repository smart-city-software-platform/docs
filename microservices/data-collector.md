# Data Collector

The Data Collector is a microservice responsible for providing methods to search data collected
from Resources. This service is used both by applications and the Catalog Service, and offers
developers access to information coming from different sensors scattered
throughout the city. For example, with this service one could obtain data of
all temperature sensors on USP. The input obtained from the sensors is stored dinamically on MongoDB, rather than on a single value field.

