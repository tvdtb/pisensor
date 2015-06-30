package com.trivadis.grovepi.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.trivadis.grovepi.model.Distance;
import com.trivadis.grovepi.service.UltrasonicService;

@Path("ultrasonic")
public class UltrasonicResource {
	
	@Inject
	UltrasonicService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Distance readDistance() {
		
		Distance distance = service.readDistance();
		return distance;
	}

}
