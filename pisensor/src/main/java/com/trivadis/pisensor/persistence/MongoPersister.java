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
import com.trivadis.pisensor.TemperatureEvent;
import com.trivadis.pisensor.persistence.mongodb.Mongo;

@ApplicationScoped
@Eager
public class MongoPersister {
	
	private static final long SECONDS = 600;

	@Inject
	@Mongo
	Datastore datastore;

	private LocalDateTime lastTime;

	private SimpleDateFormat format;

	@PostConstruct
	public void initialize() throws ParseException {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		format.setTimeZone(tz);
		
		List<DataEvent> result = datastore //
				.createQuery(DataEvent.class) //
				.order("-_id") //
				.limit(1) //
				.asList();

		this.lastTime = LocalDateTime.MIN;
		if (result.size() > 0) {
			this.lastTime = toLocal(result.get(0).getTimeOfEvent());
		}
	}

	private LocalDateTime toLocal(String iso) throws ParseException {
		Date parsed = format.parse(iso);
		LocalDateTime local = LocalDateTime.ofInstant(parsed.toInstant(), ZoneId.systemDefault());
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
		Date d = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		String iso = format.format(d);
		return iso;
	}

}
