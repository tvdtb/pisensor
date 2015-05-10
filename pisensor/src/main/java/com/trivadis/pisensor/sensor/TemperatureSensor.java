package com.trivadis.pisensor.sensor;

import com.trivadis.pisensor.TemperatureEvent;

public interface TemperatureSensor {

	public TemperatureEvent readTemperatureFromDevice();
}
