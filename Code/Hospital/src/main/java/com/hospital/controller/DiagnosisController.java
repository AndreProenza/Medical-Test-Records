package com.hospital.controller;

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

@Controller
@RequestMapping("patient/diagnosis")
public class DiagnosisController {
	
	private MedicalRecordService medicalRecordService;

	public DiagnosisController(MedicalRecordService medicalRecordService) {
		super();
		this.medicalRecordService = medicalRecordService;
	}
	
	@GetMapping("")
	public String showPatientRecordForm(Model model) {
		MedicalRecord record = new MedicalRecord();
		model.addAttribute("record", record);
		return "diagnosis";
	}
	
	//TODO
	@PostMapping("")
	public String updatePatientRecord(@ModelAttribute("record") @Valid MedicalRecord record, Errors errors) {
		if(errors.hasErrors()) {
			return "diagnosis";
		}
		return "diagnosis";
	}
}
