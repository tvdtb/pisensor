package com.trivadis.pisensor.sensor.raspberry;

import java.time.LocalDateTime;

import javax.enterprise.inject.Alternative;

import com.trivadis.pisensor.TemperatureEvent;
import com.trivadis.pisensor.sensor.TemperatureSensor;

@Alternative
public class TemperatureSensorDummy implements TemperatureSensor {

	@Override
	public TemperatureEvent readTemperatureFromDevice() {
		return new TemperatureEvent(LocalDateTime.now(), -1, 1000f);
	}

	
}
