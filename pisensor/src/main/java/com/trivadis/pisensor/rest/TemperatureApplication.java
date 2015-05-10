package com.trivadis.pisensor.rest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rs")
public class TemperatureApplication extends Application {
	
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> result = new HashSet<>();
		
		
		return result ;
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> result = new HashSet<>();
		
		result.add(TemperatureResource.class);
		
		return result;
	}
	
	@Override
	public Map<String, Object> getProperties() {
		return super.getProperties();
	}

}
