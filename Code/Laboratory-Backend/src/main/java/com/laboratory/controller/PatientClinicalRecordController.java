package com.laboratory.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.hospitalservice.HospitalService;
//import com.laboratory.hospitalservice.HospitalService;
import com.laboratory.model.mongodb.ClinicalRecord;
import com.laboratory.service.CitizenService;
import com.laboratory.service.ClinicalRecordService;

@RestController
@RequestMapping("api/patient/clinical/record")
public class PatientClinicalRecordController {
	
	private ClinicalRecordService clinicalRecordService;
	private CitizenService citizenService;
	
	
	public PatientClinicalRecordController(ClinicalRecordService clinicalRecordService, CitizenService citizenService) {
		super();
		this.clinicalRecordService = clinicalRecordService;
		this.citizenService = citizenService;
	}

	/**
	 * Registers a clinical record associated to a specific citizen id
	 * and saves clinical record in database if citizen id is associated to clinical
	 * record exists
	 * 
	 * @requires citizenId must be registered
	 * @param the clinical record to be saved
	 * @return clinical record if it is correctly saved in database, null otherwise.
	 */
	@PostMapping("/register")
	public ClinicalRecord submitClinicalRecord(@RequestBody ClinicalRecord record) {
		if(citizenService.existsCitizenById(record.getCid())) {
			if (clinicalRecordService.saveRecord(record) != null) {
				// send clinical record to hospital
				@SuppressWarnings("unused")
				boolean response = HospitalService.sendRecord(record);
			}
			return record;
		}
		return null;
	}
	
}
