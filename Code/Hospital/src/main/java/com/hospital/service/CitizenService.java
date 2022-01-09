package com.hospital.service;

import java.util.List;

import com.hospital.model.mongodb.Citizen;

public interface CitizenService {
	
	/**
	 * Saves to database a citizen record
	 * 
	 * @param citizen the citizen record
	 * @return the saved citizen record
	 */
	Citizen saveCitizen(Citizen citizen);
	
	/**
	 * Gets all citizen records
	 * 
	 * @return a list of citizen records
	 */
	List<Citizen> getAllCitizens();
	
	/**
	 * Get a citizen record by id
	 * 
	 * @param id the citizen id
	 * @return a citizen record
	 */
	Citizen getCitizenById(String id);
	
	/**
	 * Updates a citizen record
	 * Only Ward Clerks can update citizen records
	 * 
	 * @param citizen the citizen Record
	 * @param id the citizen id
	 * @return an updated citizen Record
	 */
	Citizen updateCitizenAsWardClerk(Citizen citizen, String id);
	
	/**
	 * Updates a citizen record
	 * Only Administrators can update citizen records
	 * 
	 * @param citizen the citizen Record
	 * @param id the citizen id
	 * @return an updated citizen Record
	 */
	Citizen updateCitizenAsAdministrator(Citizen citizen, String id);
	
	/**
	 * Deletes a citizen record
	 * Only a system administrator can delete citizen records
	 * 
	 * @param id the citizen Record id
	 */
	void deleteCitizen(String id);
	
	
	/**
	 * Check if citizen exixst in database
	 * 
	 * @param id the citizen id
	 * @return true if citizen exists in database, false otherwise
	 */
	boolean existsCitizenById(String id);
	
	/**
	 * Login to application
	 * 
	 * @param citizenId the citizen id
	 * @param password the citizen id password
	 * @return the citizen if registered
	 */
	Citizen login(String citizenId, String password);
}
