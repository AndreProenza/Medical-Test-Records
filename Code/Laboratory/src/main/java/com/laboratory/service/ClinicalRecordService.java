package com.laboratory.service;

import java.util.List;

import com.laboratory.model.mongodb.ClinicalRecord;

public interface ClinicalRecordService {
	
	/**
	 * Saves to database a clinical record
	 * 
	 * @param clinicalRecord the clinical record
	 * @return the saved clinical record
	 */
	ClinicalRecord saveRecord(ClinicalRecord clinicalRecord);
	
	/**
	 * Gets all clinical records
	 * 
	 * @return a list of clinical records
	 */
	List<ClinicalRecord> getAllRecords();
	
	/**
	 * Get a clinical record by id
	 * 
	 * @param id the clinical Record id
	 * @return a clinical record
	 */
	ClinicalRecord getRecordById(String id);
	
	/**
	 * Updates a clinical record
	 * Only Doctors and Nurses can update clinical records
	 * 
	 * @param clinicalRecord the clinical Record
	 * @param id the clinical Record id
	 * @return an updated clinical Record
	 */
	ClinicalRecord updateRecord(ClinicalRecord clinicalRecord, String id);
	
	/**
	 * Deletes a clinical record
	 * Only a system administrator can delete clinical records
	 * 
	 * @param id id the clinical Record id
	 */
	void deleteRecord(String id);
	
	/**
	 * Get all clinical records associated to a citizen Id
	 * 
	 * @param id the citizenId
	 * @return list of clinical records associated to a citizen Id
	 */
	List<ClinicalRecord> getAllRecordsByCitizenId(String id);
}
