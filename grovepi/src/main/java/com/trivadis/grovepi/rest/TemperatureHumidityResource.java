package com.trivadis.grovepi.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.trivadis.grovepi.model.TemperatureHumidity;
import com.trivadis.grovepi.service.TemperatureHumidityService;
import com.trivadis.grovepi.service.UltrasonicService;

@Path("temp")
public class TemperatureHumidityResource {
	
	@Inject
	TemperatureHumidityService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TemperatureHumidity readTemperatureHumidity() {
		
		TemperatureHumidity th = service.read();
		
		return th;
	}

}
