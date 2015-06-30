package com.trivadis.pisensor.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

// db.DataEvent.update({}, { $unset: { className: "" } }, { multi: true})
@Entity(noClassnameStored=true)
public class DataEvent {
	@Id
	private ObjectId id;
	private String timeOfEvent;
	private float celsius;
	private float pressure;

	public void setTimeOfEvent(String timeOfEvent) {
		this.timeOfEvent = timeOfEvent;
	}

	public void setCelsius(float celsius) {
		this.celsius = celsius;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public String getTimeOfEvent() {
		return timeOfEvent;
	}

	public float getCelsius() {
		return celsius;
	}

	public float getPressure() {
		return pressure;
	}

}
