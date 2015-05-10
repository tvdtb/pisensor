package com.trivadis.pisensor.persistence.mongodb;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.trivadis.cdi.eager.Eager;

@ApplicationScoped
@Eager
public class DatastoreProducer {

	private Datastore datastore = null;

	@PostConstruct
	public void init() {
		
		try {
			String mongoURI = "mongodb://localhost/pitemp?safe=true&w=majority";
            final MongoClientOptions.Builder cob = MongoClientOptions.builder().heartbeatFrequency(60000);
			MongoClientURI mongoClientURI = new MongoClientURI(mongoURI, cob);
			MongoClient mongoClient = new MongoClient(mongoClientURI);

			Morphia morphia = new Morphia();

			this.datastore = morphia.createDatastore(mongoClient, mongoClientURI.getDatabase());
			datastore.ensureIndexes();

			morphia.getMapper().getOptions().setStoreEmpties(true);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	@Mongo
	@Produces
	public Datastore getDatastore() {
		return datastore;
	}
}
