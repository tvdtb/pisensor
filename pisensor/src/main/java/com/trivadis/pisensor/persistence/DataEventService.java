package com.trivadis.pisensor.persistence;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.mongodb.morphia.Datastore;

import com.trivadis.pisensor.persistence.mongodb.Mongo;

public class DataEventService {

	@Inject
	@Mongo
	Datastore datastore;

	public List<DataEvent> readEvents(int offset, int limit, int scale) {
		if (scale<1)
			scale = 1;
		if (offset<0)
			offset = 0;
		limit = limit * scale;
		
		List<DataEvent> result = datastore //
				.createQuery(DataEvent.class) //
				.order("-_id") //
				.offset(offset) //
				.limit(limit) //
				.asList();


		if (scale==1) {
			return result;
		} else {
			List<DataEvent> result2 = new LinkedList<>();
			int i=scale;
			for (DataEvent de:result) {
				if (i % scale==0) {
					result2.add(de);
					i=0;
				}
				if (result2.size()==limit)
					break;
				i++;
			}
			return result2;
		}
	}

}
