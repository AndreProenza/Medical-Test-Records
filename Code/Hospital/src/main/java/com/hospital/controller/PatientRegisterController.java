package com.hospital.controller;


import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.model.mongodb.Citizen;
import com.hospital.service.CitizenService;

@Controller
@RequestMapping("api/patient/register")
public class PatientRegisterController {

	private CitizenService citizenService;
	
	public PatientRegisterController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	
	
	@GetMapping("")
	public String showForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		return "patient_register";
	}
	
	//TODO
	@PostMapping("")
	public String registerPatient(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors) {
		if(errors.hasErrors()) {
			return "patient_register";
		}
		return "patient_register";
	}
}
