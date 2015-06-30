package com.trivadis.grovepi.rest;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.trivadis.grovepi.service.LCDService;

@Path("lcd")
public class LcdResource {
	
	@Inject
	LCDService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message setText(@QueryParam("text") @DefaultValue("Hello world!") String text //
			, @QueryParam("color") @DefaultValue("0,0,0") String color //
			, @QueryParam("time") @DefaultValue("15000") long time ) {
		
		String[] rgb = color.split(",");
		
		service.setColor(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
		service.setText(text, time);
		
		return new Message("Text gesetzt");
	}

}
