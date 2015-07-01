package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.grovepi.device.DeviceThread;
import com.trivadis.grovepi.model.Distance;

@ApplicationScoped
@Eager
public class DistanceSource {

	@Inject
	UltrasonicService service;

	@Inject
	Event<Distance> event;

	@PostConstruct
	public void init() {
		new DeviceThread(2000) {
			@Override
			public void performDeviceAccess() {
				readAndPublishDistance();
			}
		}.start();
	}
	
	// @Schedule(persistent=false, second="0,15,30,45")
	public void readAndPublishDistance() {
		Distance distance = service.readDistance();
		event.fire(distance);
	}

}
