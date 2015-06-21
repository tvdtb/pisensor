package com.trivadis.pisensor.persistence;

import java.util.List;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import com.trivadis.pisensor.persistence.mongodb.Mongo;

public class DataEventService {

	@Inject
	@Mongo
	Datastore datastore;

	public List<DataEvent> readEvents(int offset, int limit) {
		List<DataEvent> result = datastore //
				.createQuery(DataEvent.class) //
				.order("-_id") //
				.offset(offset).limit(limit) //
				.asList();
		return result;
	}

}
