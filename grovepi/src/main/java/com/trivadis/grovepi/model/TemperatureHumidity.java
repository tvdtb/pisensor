package com.trivadis.grovepi.model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.trivadis.grovepi.ISODateAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TemperatureHumidity {

	/**
	 * time of measurement (local)
	 */
	@XmlJavaTypeAdapter(ISODateAdapter.class)
	private LocalDateTime datetime;
	
	private float temp;
	private float humidity;

	public TemperatureHumidity() {
		
	}
	
	public TemperatureHumidity(float temp, float humidity, LocalDateTime datetime) {
		this.datetime = datetime;
		this.temp = temp;
		this.humidity = humidity;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

}
