package com.hospitalserver;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.hospitalserver.model.mongodb.Citizen;
import com.hospitalserver.model.mongodb.MedicalRecord;
import com.hospitalserver.server.Server;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootApplication
public class HospitalBackendServerApplication {

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		MongoClient client = MongoClients.create("mongodb+srv://sirs:sirsgrupo26@hospitalcluster.csoal.mongodb.net/hospitalcluster");
		MongoDatabase database = client.getDatabase("hospital");
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		database = database.withCodecRegistry(pojoCodecRegistry);
		MongoCollection<Citizen> citizenCollection = database.getCollection("citizen", Citizen.class);
		MongoCollection<MedicalRecord> medicalRecordCollection = database.getCollection("record", MedicalRecord.class);

		Server.run(citizenCollection, medicalRecordCollection);

	}
	
	public static void main(String[] args) {
		SpringApplication.run(HospitalBackendServerApplication.class, args);
	}

}
