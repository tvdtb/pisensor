package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.grovepi.model.TemperatureHumidity;

@ApplicationScoped
@Eager
public class TemperatureSource {
	
	@Inject
	TemperatureHumidityService service;

	@Inject
	Event<TemperatureHumidity> event;

	@PostConstruct
	public void init() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						// THIS IS THE ACTUAL READ
						readAndPublishTemperature();
						Thread.sleep(10000);
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
		TemperatureHumidity tempHum = service.read();
		event.fire(tempHum);
	}
	
}
