# Smart City Platform Architecture

This page presents an overview of the microservice architecture
provided by the Smart City Software Platform.

## Summary

* [Overview](#overview)
* [Design constraints and guidelines](#design-constraints-and-guidelines)
* [Security](#security)
* [Roadmap](#roadmap)

## Overview

The project aims to provide high-level services to support 
novel applications that interact with city's resources such as bus,
street cameras, environmental sensors, and public open data. 
In order to properly provide such services to city-scale, the platform
needs to integrate a large number of heterogeneous physical devices and 
services. Thus, the platform is based on a scalable, distributed 
**microservices** architecture.

The following figure presents the platform's main services and how they
communicate:

![Platform's services architecture](../images/services_overview.png)
> Icons from [Flaticon](http://www.flaticon.com/packs/urban-3)

Communication between services are represented as directed arrows labeled by
red circles:

* **(A) Physical devices integration:** City's resources
are coupled with cyber-physical devices, such as sensors and actuators, which
may integrate to the platform by serveral different protocols. Thus, the
*Resource Adaptor* is a gateway service to which devices can register at the
platform, send and request data. Every registered resource receives an 
[UUID](https://tools.ietf.org/html/rfc4122). Checkout the supported protocols
and technical details in the [Resource Adaptor page]().
* **(B) Resource register:** In order to make a resource available on the
platform, *Resource Adaptor* sends the resource meta-data to *Resource
Cataloguer* service through its REST API. These meta-data describes the main
features of a resource, exposing its capabilities, location and other important
information.
* **(C) Resource creation notification:** After registering a new resource,
the *Resource Cataloguer* notifies *Data Collector* service if the resource
has sensor capabilities. Similarly, the *Actuator Controller* service is 
notified whenever a new resource has actuator capabilities.
* **(D) Sending actuation requests to resources**: The *Actuator Controller*
services is responsible to intermediate and register the actuation requests
to city's resources. To send those requests, the *Actuator Controller*
uses the REST API provided by the *Resource Adaptor*.
* **(E) Collecting data from resources**: The *Data Collector*
services is responsible to pooling *Resource Adaptor* services gathering new
data from resources with sensor capabilities. *This architecture is not 
suitable for various sensors and must be redesigned soon so that it can go
asynchronous, event-based and more scalable*.
* **(F) Search for resources based on context data:** The 
*Resource Discoverer* service provides a high-level API for Smart City 
applications to query registered city's resources by context data. Currently,
*Resource Discoverer* request the *Resource Cataloguer* for resource's 
location and meta-data. It also uses the *Data Collector* to filter available
resources based on sensors' data parameters.
* **(G) Request for resources' details:** The *Resource Viewer* is a 
visualization service that presents an overview of all city's resources. It 
provides a Web front-end page which may be used both for real-time data 
visualization and check resources administrative meta-data. *Resource Viewer*
uses the *Resource Cataloguer* REST API to get the available resources and
their meta-data. It also uses the *Data Collector* REST API to get the last
data from specific resources with sensor capabilities.



## Design constraints and guidelines

## Security

## Roadmap

