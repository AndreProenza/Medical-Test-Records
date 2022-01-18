package com.laboratory.controller;

import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.laboratory.model.mongodb.ClinicalRecord;
import com.laboratory.service.CitizenService;
import com.laboratory.service.ClinicalRecordService;

@Controller
@RequestMapping("patient/clinical/record")
public class PatientClinicalRecordController {
	
	private ClinicalRecordService clinicalRecordService;
	private CitizenService citizenService;
	
	
	public PatientClinicalRecordController(ClinicalRecordService clinicalRecordService, CitizenService citizenService) {
		super();
		this.clinicalRecordService = clinicalRecordService;
		this.citizenService = citizenService;
	}

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
	 */
	@PostMapping("")
	public String submitClinicalRecord(@ModelAttribute("record") @Valid ClinicalRecord record, Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "clinical_record";
		}
		if(isFormValid(record, model) && citizenService.existsCitizenById(record.getCid())) {
			clinicalRecordService.saveRecord(record);
			model.addAttribute("message", "Clinical Details");
			model.addAttribute("citizen",citizenService.getCitizenById(record.getCid()));
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
