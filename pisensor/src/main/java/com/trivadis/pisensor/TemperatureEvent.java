package com.trivadis.pisensor;

import java.time.LocalDateTime;

public class TemperatureEvent {

	/**
	 * time of measurement (local)
	 */
	private LocalDateTime time;
	/** 
	 * temperature measured in degrees celsius
	 */
	private float temperature;
	/**
	 * pressure measured in pascal
	 */
	private float pressure;

	public TemperatureEvent() {
	}

	public TemperatureEvent(LocalDateTime time, float readTemperature,
			float readPressure) {
		this.time = time;
		this.temperature = readTemperature;
		this.pressure = readPressure;

	}


	@Override
	public String toString() {
		return "Temperature [time=" + time + ", temperature=" + temperature
				+ ", pressure=" + pressure + "]";
	}

	public LocalDateTime getTime() {
		return time;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getPressure() {
		return pressure;
	}


}
