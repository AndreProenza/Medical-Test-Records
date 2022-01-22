package com.hospitalserver.service;

public interface CitizenService {
	

	/**
	 * Check if citizen exixst in database
	 * 
	 * @param id the citizen id
	 * @return true if citizen exists in database, false otherwise
	 */
	boolean existsCitizenById(String id);

}
