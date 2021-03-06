package com.laboratory.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.laboratory.model.Citizen;
import com.laboratory.model.ClinicalRecord;
import com.laboratory.utils.BackendUri;
import com.laboratory.utils.PatientRecord;

@Controller
@RequestMapping("clinical/records")
public class ClinicalRecordController {
	
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
		
		List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(BackendUri.CLINICAL_RECORDS_ALL, PatientRecord[].class));
		ClinicalRecord record = new ClinicalRecord();
		
		model.addAttribute("records", records);
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "clinical_records";
	}
	
	
	/**
	 * Get all records from a specific patient
	 * 
	 * @param record the clinical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("/get")
	public String getCitizenRecords(@ModelAttribute("record") ClinicalRecord record, Model model) {
		
		Boolean existsCitizen = restTemplate.getForObject(BackendUri.CITIZEN_EXISTS_GET, Boolean.class, record.getCid());
		if(!existsCitizen.booleanValue()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "clinical_records";
		}
		
		List<PatientRecord> clinicalRecords = Arrays.asList(restTemplate.getForObject(BackendUri.CLINICAL_RECORDS_GET, PatientRecord[].class, record.getCid()));
		
		if(clinicalRecords.isEmpty()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "clinical_records";
		}
		else {
			Citizen existingCitizen = restTemplate.getForObject(BackendUri.CITIZEN_GET, Citizen.class, record.getCid());
			model.addAttribute("records", clinicalRecords);
			model.addAttribute("citizen", existingCitizen);
			return "patient_record";			
		}
	}
	
}
