package com.hospital.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.hospital.model.Citizen;
import com.hospital.model.MedicalRecord;
import com.hospital.utils.BackendUri;
import com.hospital.utils.PatientRecord;

@Controller
@RequestMapping("medical/records")
public class MedicalRecordController {
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Loads all patient records from data base and set fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String loadAllRecords(Model model) {		
		
		List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(BackendUri.MEDICAL_RECORDS_ALL, PatientRecord[].class));	
		MedicalRecord record = new MedicalRecord();
		
		model.addAttribute("records", records);
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "medical_records";
	}
	
	
	/**
	 * Get all records from a specific patient
	 * 
	 * @param record the medical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("/get")
	public String getCitizenRecords(@ModelAttribute("record") MedicalRecord record, Model model) {

		Boolean existsCitizen = restTemplate.getForObject(BackendUri.CITIZEN_EXISTS_GET, Boolean.class, record.getCid());
		if(!existsCitizen.booleanValue()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "medical_records";
		}
		
		List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(BackendUri.MEDICAL_RECORDS_GET, PatientRecord[].class, record.getCid()));

		if(records.isEmpty()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "medical_records";
		}
		else {
			Citizen existingCitizen = restTemplate.getForObject(BackendUri.CITIZEN_GET, Citizen.class, record.getCid());
			model.addAttribute("records", records);
			model.addAttribute("citizen", existingCitizen);
			return "patient_record";			
		}
	}
}
