package com.trivadis.pisensor.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

	@Context
	UriInfo uriInfo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TemperatureEvent readTemp() {
		TemperatureEvent temperature = tempSource.readTemp();
		return temperature;
	}

	@GET
	@Path("/history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readTempHistory( //
			@QueryParam("offset") @DefaultValue("0") int offset //
			, @QueryParam("limit") @DefaultValue("100") int limit
			, @QueryParam("scale") @DefaultValue("1") int scale) {

		final Link next = Link.fromUriBuilder( //
				uriInfo.getBaseUriBuilder() //
						.path(TemperatureResource.class) //
						.path(TemperatureResource.class, "readTempHistory") //
						.queryParam("offset", offset + limit) //
						.queryParam("limit", limit) //
						.queryParam("scale", scale) //
						.scheme(null).host(null).port(-1) // server-absolute
				).rel("next").build();

		List<DataEvent> readEvents = eventService.readEvents(offset, limit + 1, scale);
		
		boolean more = false;
		if (readEvents.size() > limit) {
			readEvents.remove(limit);
			more = true;
		}
		DataEvent[] array = readEvents.toArray(new DataEvent[readEvents.size()]);

		if (more) {
			return Response.ok(array) //
					.links(next) //
					.build();
		} else {
			return Response.ok(array) //
					.build();
		}
	}

}
