package com.trivadis.pisensor.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.pisensor.ISODateAdapter;
import com.trivadis.pisensor.TemperatureEvent;
import com.trivadis.pisensor.persistence.mongodb.Mongo;

@ApplicationScoped
@Eager
public class MongoPersister {
	
	private static final long SECONDS = 600;

	@Inject
	@Mongo
	Datastore datastore;

	@Inject
	ISODateAdapter iso;
	
	private LocalDateTime lastTime;


	@PostConstruct
	public void initialize() {
//		List<DataEvent> result = datastore //
//				.createQuery(DataEvent.class) //
//				.order("-_id") //
//				.limit(1) //
//				.asList();

		this.lastTime = LocalDateTime.MIN;
//		if (result.size() > 0) {
//			try {
//				this.lastTime = toLocal(result.get(0).getTimeOfEvent());
//			} catch (ParseException e) {
//				// ignore
//			}
//		}
	}

	private LocalDateTime toLocal(String isoString) throws ParseException {
		LocalDateTime local = iso.unmarshal(isoString);
		return local;
	}
	
	public void event(@Observes TemperatureEvent te) {
		if (lastTime.plusSeconds(SECONDS).isBefore(te.getTime())) {
			String iso = toISO(te.getTime());
			DataEvent event = new DataEvent();
			event.setTimeOfEvent(iso);
			event.setCelsius(te.getTemperature());
			event.setPressure(te.getPressure());
			datastore.save(event);
			
			lastTime = te.getTime();
		}
	}

	private String toISO(LocalDateTime dateTime) {
		String isoString = iso.marshal(dateTime);
		return isoString;
	}

}
