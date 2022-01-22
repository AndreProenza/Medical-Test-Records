package com.laboratory.hospitalservice;

import com.laboratory.model.mongodb.ClinicalRecord;
import com.hospitalserver.model.mongodb.MedicalRecord;

public class HospitalService {

	private static String ip = "localhost";
	private static int porta = 4000;

	public static boolean sendRecord(ClinicalRecord record) {

		Client client = new Client(ip, porta);
		client.connect();
		if (client.authenticate()) {
			
			MedicalRecord mr = new MedicalRecord(record.getCid(), 0, 0, record.getBloodType(),
					record.getBloodPressure(), record.getBloodTemperature(), null, record.getDate());
			boolean ret = client.communication(mr);
			client.close();
			return ret;
		}
		System.out.println("Closing socket");
		client.close();
		return false;
	}

}
