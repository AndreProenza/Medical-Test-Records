package com.hospital.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.mongodb.Citizen;
import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.CitizenService;
import com.hospital.service.MedicalRecordService;
import com.hospital.utils.PatientRecord;

@RestController
@RequestMapping("my/medical/record")
public class MyMedicalRecordController {

	private MedicalRecordService medicalRecordService;
	private CitizenService citizenService;
	
	
	public MyMedicalRecordController(MedicalRecordService medicalRecordService, CitizenService citizenService) {
		super();
		this.medicalRecordService = medicalRecordService;
		this.citizenService = citizenService;
	}
	
	/**
	 * Get all patient records from a specific citizen
	 * 
	 * @param citizenId the citizen Id
	 * @return the list of patient records
	 */
	@GetMapping("/get/{id}")
	public List<PatientRecord> getCitizenRecords(@PathVariable("id") String citizenId) {
			List<MedicalRecord> medicalRecords = medicalRecordService.getAllRecordsByCitizenId(citizenId);
			Citizen citizen = citizenService.getCitizenById(citizenId);
			return getAllPatientRecords(medicalRecords, citizen);
	}
	
	/**
	 * Get a default patient record from a specific citizen if citizen has no records
	 * 
	 * @param citizenId the citizen Id
	 * @return the patient default null record
	 */
	@GetMapping("/default/{id}")
	public PatientRecord getDefaultCitizenRecord(@PathVariable("id") String citizenId) {
			Citizen citizen = citizenService.getCitizenById(citizenId);
			return getDefaultNullPatientRecord(citizen);
	}
	
	
	
	/**
	 * Get all patient records in database and saves them in a list of PatientRecord
	 * 
	 * @param medicalRecord the list of medical records
	 * @param citizen the citizen id
	 * @requires each medical record citizen id must be equal to the citizen id 
	 * @return the list of all records associated to citizen
	 */
	private List<PatientRecord> getAllPatientRecords(List<MedicalRecord> medicalRecords, Citizen citizen) {
		
		List<PatientRecord> records = new ArrayList<>();
		
		for(MedicalRecord medicalRecord : medicalRecords) {	
			PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
					citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
					citizen.getBirthday(), citizen.getCity(), medicalRecord.getHeight(),
					medicalRecord.getWeight(), medicalRecord.getBloodType(),
					medicalRecord.getBloodPressure(), medicalRecord.getBloodTemperature(),
					medicalRecord.getDiagnosis(), medicalRecord.getDate());
			records.add(patientRecord);
		}
		return records;
	}
	
	private PatientRecord getDefaultNullPatientRecord(Citizen citizen) {
		PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
				citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(), 
				citizen.getBirthday(), citizen.getCity(), 0, 0, "null", 0, 0, "null", "null");
		return patientRecord;
	}
}
