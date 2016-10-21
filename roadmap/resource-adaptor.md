# Roadmap - Resource Adaptor

This document describes a design roadmap for Resource Adaptor. The proposed
design modifies the purpose of a Resource Adaptor so that it behaves as a proxy
for physical devices and external services. This conceptual change may also
affect other microservices, such as Data Collector. Here are the consequences
of this design:

* **Decoupling:** Any code that manages a specific device or external service
will not be coupled to Resource Adaptor codebase anymore
* **Proxy:** As a proxy, Resource Adaptor can provide its services through
different protocols (i.e.: HTTP REST, CoAP, MQTT)
* **Resource registering:** a Resource Adaptor must provide a service to 
allow city's resources to register in the platform
* **Resource authentication:** Any resource must authenticate to become 
available in the platform. The Resource Adaptor must provide the necessary
services to enable this authentication.
* **Resource update:** A resource must be able to update its meta-data and
attributes through the Resource Adaptor API
* **Data collection:** The Data Collector won't keep polling Resource Adaptor 
for new sensor data anymore. Instead, Data Collector must provide an API to
post new sensor data, whether they are generated periodically or by events.
* **Notify actuators:** A Resource Adaptor must provide an easy-to-use 
publish/subscribe service so that resource with actuator capabilities can
receive notifications whenever a new actuator request is created in the 
platform
* **Data storing:** A Resource Adaptor will no longer provide tradditional
database-based persistence. Instead, it may be as thinner as possible so that
it can manage resources requests to send and receive data
* **Development toolkit:** Specific programming languages libraries must be
developed to wrap Resource Adaptor service details, such as protocols and 
messages.

The following figure highlight the main decisions of the proposal design.

![ResourceAdaptor](../images/resource_adaptor_roadmap.png)

## Definitions

### Resource Model

Each resource is composed by general [Meta-data](#metadata)
(i.e.: type, latitude, longitude), behavior ([Capabilities](#capabilities)),
and by specific information data ([Attributes](#attributes)).

### Metadata

* Type
* Description
* UUID
* Lat
* Lon

### Types of Capabilities

Capabilities are functional part of a resource, defining resource's behavior.
These capabilities belong to one of these types:

* **Periodic Sensor**: sensor that collect data in a fixed time period.
Examples:
  * A temperature sensor collecting data inside a room each 5 minutes
  * A luminosity sensor collecting data in a public lamppost every 30 seconds
* **Actuator**: receives a command to change its state. Each actuator
capability has a set of acceptable states or a range of values. Examples:
  * A traffic light has three possible states (green, yellow and red)
  * An air conditioner can receive an command to change its temperature.
  Possible values may vary within an acceptable temperature range.
* **Event-based**: capabilities that generate data whenever a determined event
happens. Example:
  * A presence sensor that only register an event data when someone enters in
  the monitored room
  * A parking space that register whenever a car vacates a monitored parking
  spot
* **RFID**: *Not supported yet*

## Attributes

Attributes are specific data that characterize a resource type. In most cases,
these attributes are fixed, but can be updated. For example, a parking space
may have the following attributes:

* Parking identification number - *Integer*
* Priority parking -  *Boolean*
* Operating hours - *Time range*
* Price - *Float* 


