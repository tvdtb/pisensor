package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.grovepi.device.DeviceThread;
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
		new DeviceThread("temp", 5000) {
			@Override
			public void performDeviceAccess() {
				readAndPublishTemperature();
			}
		}.start();
	}
		

//	@Schedule(persistent=false, second="0,15,30,45")
	public void readAndPublishTemperature() {
		TemperatureHumidity tempHum = service.read();
		event.fire(tempHum);
	}
	
}
