package com.laboratory.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.model.mongodb.ClinicalRecord;
import com.laboratory.service.CitizenService;
import com.laboratory.service.ClinicalRecordService;
import com.laboratory.utils.PatientRecord;

@RestController
@RequestMapping("api/clinical/records")
public class ClinicalRecordController {
	
	private ClinicalRecordService clinicalRecordService;
	private CitizenService citizenService;

	public ClinicalRecordController(ClinicalRecordService clinicalRecordService, CitizenService citizenService) {
		super();
		this.clinicalRecordService = clinicalRecordService;
		this.citizenService = citizenService;
	}
	
	
	/**
	 * Loads all patient records from data base
	 * 
	 * @return the list of patient records
	 */
	@GetMapping("")
	public List<PatientRecord> loadAllRecords() {		
		return getAllPatientsRecords();	
	}
	
	
	/**
	 * Get all records from a specific patient
	 * 
	 * @param record the clinical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("/get/{id}")
	public List<PatientRecord> getCitizenRecords(@PathVariable("id") String citizenId) {
		List<ClinicalRecord> clinicalRecords = clinicalRecordService.getAllRecordsByCitizenId(citizenId);
		Citizen citizen = citizenService.getCitizenById(citizenId);
		return getAllPatientRecords(clinicalRecords, citizen);
	}
	
	
	/**
	 * Get all patients records in database and saves them in a list of PatientRecord
	 * 
	 * @return the list of all records associated to each patient
	 */
	private List<PatientRecord> getAllPatientsRecords() {
		
		List<ClinicalRecord> clinicalRecords = clinicalRecordService.getAllRecords();
		List<Citizen> citizens = citizenService.getAllCitizens();
		List<PatientRecord> records = new ArrayList<>();
		
		for(ClinicalRecord clinicalRecord : clinicalRecords) {
			for(Citizen citizen : citizens) {
				if(clinicalRecord.getCid().equals(citizen.getId())) {
					PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
							citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
							citizen.getBirthday(), citizen.getCity(), clinicalRecord.getBloodType(),
							clinicalRecord.getBloodPressure(), clinicalRecord.getBloodTemperature(),
							clinicalRecord.getDate());
					records.add(patientRecord);
					break;
				}
			}
		}
		return records;
	}
	
	
	/**
	 * Get all patient records in database and saves them in a list of PatientRecord
	 * 
	 * @param clinicalRecords the list of clinical records
	 * @param citizen the citizen id
	 * @requires each clinical record citizen id must be equal to the citizen id 
	 * @return the list of all records associated to citizen
	 */
	private List<PatientRecord> getAllPatientRecords(List<ClinicalRecord> clinicalRecords, Citizen citizen) {
		
		List<PatientRecord> records = new ArrayList<>();
		
		for(ClinicalRecord clinicalRecord : clinicalRecords) {	
			PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
					citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
					citizen.getBirthday(), citizen.getCity(), clinicalRecord.getBloodType(),
					clinicalRecord.getBloodPressure(), clinicalRecord.getBloodTemperature(),
					clinicalRecord.getDate());
			records.add(patientRecord);
		}
		return records;
	}
	
}
