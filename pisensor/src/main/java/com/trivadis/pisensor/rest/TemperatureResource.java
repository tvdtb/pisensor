package com.trivadis.pisensor.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.trivadis.pisensor.TemperatureEvent;
import com.trivadis.pisensor.persistence.DataEvent;
import com.trivadis.pisensor.persistence.DataEventService;
import com.trivadis.pisensor.sensor.TemperatureSource;

@Path("temperature")
public class TemperatureResource {

	@Inject
	TemperatureSource tempSource;
	
	@Inject
	DataEventService eventService;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String readTemp() {
		TemperatureEvent temperature = tempSource.readTemp();
		return "result = "+temperature;
	}
	
	@GET
	@Path("/history")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DataEvent> readTempHistory() {
		return eventService.readEvents();
	}
	
}
