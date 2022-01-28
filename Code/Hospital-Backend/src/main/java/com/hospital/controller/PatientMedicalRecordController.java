package com.hospital.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.CitizenService;
import com.hospital.service.MedicalRecordService;

@RestController
@RequestMapping("api/patient/medical/record")
public class PatientMedicalRecordController {
	
	private MedicalRecordService medicalRecordService;
	private CitizenService citizenService;
	
	public PatientMedicalRecordController(MedicalRecordService medicalRecordService, CitizenService citizenService) {
		super();
		this.medicalRecordService = medicalRecordService;
		this.citizenService = citizenService;
	}
	
	
	/**
	 * Registers a medical record associated to a specific citizen id
	 * and saves medical record in database if citizen id is associated to medical
	 * record exists
	 * 
	 * @requires citizenId must be registered
	 * @param the medical record to be saved
	 * @return medical record if it is correctly saved in database, null otherwise.
	 */
	@PostMapping("/register")
	public MedicalRecord submitMedicalRecord(@RequestBody MedicalRecord record) {
		if(citizenService.existsCitizenById(record.getCid())) {
			medicalRecordService.saveRecord(record);
			return record;
		}
		return null;
	}
}
