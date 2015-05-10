# pisensor

pisensor is a demo project to show the JEE capabilities of Raspberry PI using sensors, mondogb persistence and REST/websocket interface.

## Index

* [Getting started](#getting-started)
* [Core concepts](#core-concepts)

## Getting started
* Raspberry PI (I used model B+ running archlinux)
* Adafruit BMP180 sensor
* Java8, Maven
* local mongodb
* Tomcat or Wildfly (activate the maven profile "tomcat" for tomcat)
* i2c Access (activate alternative in beans.xml for local tests)

* [Websocket access](http://localhost:8080/pisensor/index.html)
* [REST, current temperature](http://localhost:8080/pisensor/rs/temperature)  
* [REST, temperature history](http://localhost:8080/pisensor/rs/temperature/history)  

## Core concepts
* pisensor uses eagerly initialized CDI beans using a portable extendsion.
* data is published actively by CDI events every few seconds
* RESTlets, WebSocketEndpoints and mongodb persistence are CDI event consumers and provide the latest data
