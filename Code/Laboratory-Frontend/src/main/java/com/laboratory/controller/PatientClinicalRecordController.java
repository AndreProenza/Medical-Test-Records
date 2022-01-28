package com.laboratory.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.laboratory.model.Citizen;
import com.laboratory.model.ClinicalRecord;
import com.laboratory.utils.BackendUri;

@Controller
@RequestMapping("patient/clinical/record")
public class PatientClinicalRecordController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Shows form and set form fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String showPatientRecordForm(Model model) {		
		ClinicalRecord record = new ClinicalRecord();
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "clinical_record";
	}
	
	
	/**
	 * Registers a clinical record associated to a specific citizen id
	 * Reads form, validate it and saves clinical record in database if citizen id is associated to clinical
	 * record exists
	 * 
	 * @requires citizenId registered
	 * @param record the clinical record to be created
	 * @param errors the form errors
	 * @param model the model
	 * @return the page to display
	 * @throws URISyntaxException 
	 */
	@PostMapping("")
	public String submitClinicalRecord(@ModelAttribute("record") @Valid ClinicalRecord record, Errors errors, Model model) throws URISyntaxException {
		if(errors.hasErrors()) {
			return "clinical_record";
		}
		if(isFormValid(record, model)) {
			
			URI uri = new URI(BackendUri.CLINICAL_RECORD_POST);
			ClinicalRecord clinicalRecord = restTemplate.postForObject(uri, record, ClinicalRecord.class);
			
			//If clinical record was not saved sucessfully
			if(clinicalRecord == null) {
				model.addAttribute("message", "Error creating clinical record!\nCitizen ID not registered\n");
				return "clinical_record";
			}
			Citizen existingCitizen = restTemplate.getForObject(BackendUri.CITIZEN_GET, Citizen.class, record.getCid());
			
			model.addAttribute("message", "Clinical Details");
			model.addAttribute("citizen", existingCitizen);
		}
		else {
			model.addAttribute("message", "Error creating clinical record!\nCitizen ID not registered\n");
			return "clinical_record";
		}
		return "record_success";
	}
	
	
	/**
	 * Check if field form are valid
	 * 
	 * @param record the clinical record
	 * @param model the model
	 * @return true if all fields are correct, false otherwise
	 */
	private boolean isFormValid(ClinicalRecord record, Model model) {
		boolean validField;
		
		validField = Pattern.compile("^[0-9]{8}$").matcher(record.getCid()).find();
		if(!validField || record.getCid() == null) {
			model.addAttribute("id", "Citizen id must be valid");
			return false; 
		}
		if(record.getBloodType() == null) {
			model.addAttribute("bloodType", "Blood Type must be valid");
			return false; 
		}
		if(record.getBloodPressure() > 300 ||  record.getBloodPressure() < 1) {
			model.addAttribute("bloodpressure", "Blood Pressure must be valid");
			return false; 
		}
		if(record.getBloodTemperature() > 45 ||  record.getBloodTemperature() < 30) {
			model.addAttribute("bodyTemp", "Body Temperature must be valid");
			return false; 
		}
		if(record.getDate() == null) {
			model.addAttribute("date", "Clinical record Date must be valid");
			return false; 
		}
		return true;
	}
}
