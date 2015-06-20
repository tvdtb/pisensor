package com.trivadis.pisensor.sensor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.pisensor.TemperatureEvent;

@ApplicationScoped
@Eager
public class TemperatureSource {
	
	@Inject
	TemperatureSensor sensor;

	@Inject
	Event<TemperatureEvent> event;

	volatile TemperatureEvent current;

	@PostConstruct
	public void init() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						// THIS IS THE ACTUAL READ
						readAndPublishTemperature();
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		thread.setDaemon(true);
		thread.start();
	}

//	@Schedule(persistent=false, second="0,15,30,45")
	public void readAndPublishTemperature() {
		TemperatureSource.this.current = sensor.readTemperatureFromDevice();
		event.fire(TemperatureSource.this.current);
	}
	
	public TemperatureEvent readTemp() {
		return current;
	}
}
