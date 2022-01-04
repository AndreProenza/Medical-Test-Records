package com.hospital.service;

import java.util.List;

import com.hospital.model.mongodb.MedicalRecord;

public interface MedicalRecordService {
	
	/**
	 * Saves to database a medical record
	 * 
	 * @param medicalRecord the medical record
	 * @return the saved medical record
	 */
	MedicalRecord saveRecord(MedicalRecord medicalRecord);
	
	/**
	 * Gets all medical records
	 * 
	 * @return a list of medical records
	 */
	List<MedicalRecord> getAllRecords();
	
	/**
	 * Get a medical record by id
	 * 
	 * @param id the medical Record id
	 * @return a medical record
	 */
	MedicalRecord getRecordById(String id);
	
	/**
	 * Updates a medical record
	 * Only Doctors and Nurses can update medical records
	 * 
	 * @param medicalRecord the medical Record
	 * @param id the medical Record id
	 * @return an updated medical Record
	 */
	MedicalRecord updateRecord(MedicalRecord medicalRecord, String id);
	
	/**
	 * Deletes a medical record
	 * Only a system administrator can delete medical records
	 * 
	 * @param id id the medical Record id
	 */
	void deleteRecord(String id);
}
