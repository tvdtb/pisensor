# pisensor

pisensor is a demo project to show the JEE capabilities of Raspberry PI using sensors, mondogb persistence and REST/websocket interface.

## Index

* [Getting started](#getting-started)
* [Core concepts](#core-concepts)
* [Raspberry Installation](#installation)
* [Build'n run](#buildnrun)

## Getting started <a name="getting-started"></a>
* Raspberry PI (I used model B+ running archlinux)
* Adafruit BMP180 sensor
* Java8, Maven
* local mongodb
* Tomcat or Wildfly (activate the maven profile "tomcat" for tomcat)
* i2c Access (activate alternative in beans.xml for local tests)

* [Websocket access](http://localhost:8080/pisensor/index.html)
* [REST, current temperature](http://localhost:8080/pisensor/rs/temperature)  
* [REST, temperature history](http://localhost:8080/pisensor/rs/temperature/history)  

## Core concepts <a name="core-concepts"></a>
* pisensor uses eagerly initialized CDI beans using a portable extendsion.
* data is published actively by CDI events every few seconds
* RESTlets, WebSocketEndpoints and mongodb persistence are CDI event consumers and provide the latest data

## Installation <a name="installation"></a>
Download and install [archlinux-arm](http://archlinuxarm.org/platforms/armv6/raspberry-pi) to your sd card

Login to the pi, the default login is root with password root:
 
    ssh root@192.168....

Update the distribution: 

    pacman -Syu
    
Activate ntp: 

    timedatectl set-ntp true
    timedatectl set-timezone Europe/Berlin``
- check for timezone using ``timedatectl list-timezones``

Install i2c:

    pacman -Sy i2c-tools

configure
    
    vi /boot/config.txt``
Mine works with the following lines (excerpt)

    device_tree_param=i2c_arm=on
    device_tree=bcm2708-rpi-b-plus.dtb
    #device_tree_param=i2c_vc=on
    #device_tree_param=i2s=on
    device_tree_param=spi=on
    #device_tree_param=act_led_trigger=mmc
  
Append a line ``i2c-dev`` to /etc/modules-load.d/raspberrypi.conf

Add the i2c group:

    groupadd i2c

Configure to use the group

    vi /lib/udev/rules.d/60-i2c-tools.rules
put these lines in: 

    KERNEL=="i2c-0"     , GROUP="i2c", MODE="0660"
    KERNEL=="i2c-[1-9]*", GROUP="i2c", MODE="0666"

Create the pi user, including groups assignment:
    
    useradd -m --groups i2c pi
    
Reboot

Check

    i2cdetect -y 1
Should result in (for boot, root and pi)
    
        0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
    00: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    70: -- -- -- -- -- -- -- --

Wire the sensor according to [this](http://www.lediouris.net/RaspberryPI/BMP180/readme.html)

## Build'n run <a name="buildnrun"></a>

Install JDK 8 from Oracle

Build cdiext

	mvn clean install

Package and install pisensor

For non-raspberry tests omit the "raspberry" profile during maven builds and it will give you dummy data on any computer

### Tomcat
For [tomcat](http://tomcat.apache.org/) - run

	mvn -P servlet-only clean package
	
and deploy the resulting pisensor.war to tomcat

	http://localhost:8080/pisensor/rs/temperature

### Wildfly-Swarm
For [wildfly-swarm](https://github.com/wildfly-swarm/wildfly-swarm) run

    mvn -P raspberry,wildfly-swarm clean package

and run the resulting pisensor-swarm.jar using

	java -jar pisensor-swarm.jar
	http://localhost:8080/rs/temperature

### Payara-Micro	
For [payara-micro](http://www.payara.co.uk/downloads) run 

    mvn -P raspberry package

and then run

	java -jar payara-micro-4.1.152.1.jar --deploy pisensor.war
	http://localhost:8080/pisensor/rs/temperature
	

	



