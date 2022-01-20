package com.laboratory.hospitalservice;

import com.laboratory.model.mongodb.ClinicalRecord;

public class HospitalService {
	
	private static String ip = "localhost";
	private static int porta = 4000;
	
	public static void sendRecord(ClinicalRecord record) {
		
		Client client = new Client(ip, porta);
		client.connect();
		client.authenticate();
		client.sendRecord(record);
	}

	
}
