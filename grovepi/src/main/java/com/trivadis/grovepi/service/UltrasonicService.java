package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.trivadis.grovepi.device.UltrasonicSensor;
import com.trivadis.grovepi.model.Distance;

@Singleton
public class UltrasonicService {

	private UltrasonicSensor sensor;

	@PostConstruct

	public void init() {
		this.sensor = new UltrasonicSensor();
		sensor.init();
	}

	public Distance readDistance() {
		return sensor.readDistance();
	}

}
