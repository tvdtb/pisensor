package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.trivadis.cdi.eager.Eager;
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
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						// THIS IS THE ACTUAL READ
						readAndPublishDistance();
						Thread.sleep(2000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		thread.setDaemon(true);
		thread.start();
	}

	// @Schedule(persistent=false, second="0,15,30,45")
	public void readAndPublishDistance() {
		Distance distance = service.readDistance();
		event.fire(distance);
	}

}
