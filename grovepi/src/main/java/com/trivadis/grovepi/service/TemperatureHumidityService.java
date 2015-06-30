package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.trivadis.grovepi.device.TemperatureSensor;
import com.trivadis.grovepi.model.TemperatureHumidity;

@Singleton
public class TemperatureHumidityService {

	private TemperatureSensor sensor;

	@PostConstruct
	public void init() {
		this.sensor = new TemperatureSensor();
		sensor.init();
	}

	public TemperatureHumidity read() {
		return sensor.read();
	}

}
