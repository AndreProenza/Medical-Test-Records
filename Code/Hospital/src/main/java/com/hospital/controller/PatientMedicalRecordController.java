package com.hospital.controller;

import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.MedicalRecordService;
import com.hospital.service.CitizenService;

@Controller
@RequestMapping("patient/medical/record")
public class PatientMedicalRecordController {
	
	private MedicalRecordService medicalRecordService;
	private CitizenService citizenService;
	
	public PatientMedicalRecordController(MedicalRecordService medicalRecordService, CitizenService citizenService) {
		super();
		this.medicalRecordService = medicalRecordService;
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
		MedicalRecord record = new MedicalRecord();
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "medical_record";
	}
	
	
	/**
	 * Registers a medical record associated to a specific citizen id
	 * Reads form, validate it and saves medical record in database if citizen id is associated to medical
	 * record exists
	 * 
	 * @requires citizenId registered
	 * @param record the medical record to be created
	 * @param errors the form errors
	 * @param model the model
	 * @return the page to display
	 */
	@PostMapping("")
	public String submitMedicalRecord(@ModelAttribute("record") @Valid MedicalRecord record, Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "medical_record";
		}
		if(isFormValid(record, model) && citizenService.existsCitizenById(record.getCid())) {
			medicalRecordService.saveRecord(record);
			model.addAttribute("message", "Medical Details");
			model.addAttribute("citizen",citizenService.getCitizenById(record.getCid()));
		}
		else {
			model.addAttribute("message", "Error creating medical record!\nCitizen ID not registered\n");
			return "medical_record";
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
	private boolean isFormValid(MedicalRecord record, Model model) {
		boolean validField;
		
		validField = Pattern.compile("^[0-9]{8}$").matcher(record.getCid()).find();
		if(!validField || record.getCid() == null) {
			model.addAttribute("id", "Citizen id must be valid");
			return false; 
		}
		if(record.getHeight() < 50 || record.getHeight() > 300) {
			model.addAttribute("height", "Height must be valid");
			return false; 
		}
		if(record.getWeight() < 1 || record.getWeight() > 300) {
			model.addAttribute("weight", "Weight must be valid");
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
		if(record.getDiagnosis() == null || record.getDiagnosis().length() < 0 ||
				record.getDiagnosis().length() > 50) {
			model.addAttribute("diagnosis", "Diagnosis must be valid");
			return false; 
		}
		if(record.getDate() == null) {
			model.addAttribute("date", "Medical record Date must be valid");
			return false; 
		}
		return true;
	}
}
