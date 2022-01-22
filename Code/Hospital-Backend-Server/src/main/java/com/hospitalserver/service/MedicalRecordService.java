package com.hospitalserver.service;

import com.hospitalserver.model.mongodb.MedicalRecord;

public interface MedicalRecordService {
	
	/**
	 * Saves to database a medical record
	 * 
	 * @param medicalRecord the medical record
	 * @return the saved medical record
	 */
	MedicalRecord saveRecord(MedicalRecord medicalRecord);

}
