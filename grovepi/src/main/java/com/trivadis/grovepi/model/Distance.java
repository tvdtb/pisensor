package com.trivadis.grovepi.model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.trivadis.grovepi.ISODateAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Distance {

	/**
	 * time of measurement (local)
	 */
	@XmlJavaTypeAdapter(ISODateAdapter.class)
	private LocalDateTime datetime;
	
	private float distance;

	public Distance() {
		
	}
	
	public Distance(float distance, LocalDateTime datetime) {
		this.distance = distance;
		this.datetime = datetime;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
}
