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

import com.hospital.utils.PatientRecord;
import com.hospital.model.Citizen;
import com.hospital.model.MedicalRecord;

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
		
		String uri = "medical/records";
		List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(uri, PatientRecord[].class));	
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
		
		String uri = "citizen/exists/{id}";
		Boolean existsCitizen = restTemplate.getForObject(uri, Boolean.class, record.getCid());
		if(!existsCitizen.booleanValue()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "medical_records";
		}
		
		uri = "medical/records/get/{id}";
		List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(uri, PatientRecord[].class, record.getCid()));

		if(records.isEmpty()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "medical_records";
		}
		else {
			uri = "citizen/get/{id}";
			Citizen existingCitizen = restTemplate.getForObject(uri, Citizen.class, record.getCid());
			model.addAttribute("records", records);
			model.addAttribute("citizen", existingCitizen);
			return "patient_record";			
		}
	}
}
