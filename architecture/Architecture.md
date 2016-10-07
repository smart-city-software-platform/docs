# Smart City Platform Architecture

This page presents an overview of the microservice architecture
provided by the Smart City Software Platform.

## Summary

* (Overview)[]
* (Design constraints and guidelines)[]
* (Security)[]
* (Roadmap)[]

## Overview

The project aims to provide high-level services to support 
novel applications that interact with city's resources such as bus,
street cameras, environmental sensors, and public open data. 
In order to properly provide such services to city-scale, the platform
needs to integrate a large number of heterogeneous physical devices and 
services. Thus, the platform is based on a scalable, distributed 
**microservices** architecture.

Here is the platform's services in layers:

![Platform's services layers][../images/layer_overview.png]
