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

import com.hospital.model.mongodb.Citizen;
import com.hospital.service.CitizenService;
import com.hospital.utils.PasswordGenerator;

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
	public String registerPatient(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors, Model model) {
		//If form has errors
		if(!(errors.getErrorCount() == 2 && errors.hasFieldErrors("role") && errors.hasFieldErrors("password"))) {
			return "patient_register";
		}
		//Generate password and send to citizen email
		String new_password = PasswordGenerator.generate(16);
		citizen.setPassword(new_password);
		
		//Atribute Role to citizen
		citizen.setRole("Patient");
		
		if(isFormValid(citizen, model) && !citizenService.existsCitizenById(citizen.getId())) {
			citizenService.saveCitizen(citizen);
			return "registration_success";
		}
		model.addAttribute("citizenAlreadyExists", "Patient is already registered");
		return "patient_register";			
		
	}
	
	private boolean isFormValid(Citizen citizen, Model model) {
		boolean validField;
		
		validField = Pattern.compile("^[0-9]{8}$").matcher(citizen.getId()).find();
		if(!validField || citizen.getId() == null) {
			model.addAttribute("id", "Citizen id must be valid");
			return false; 
		}
		if(citizen.getRole() == null) {
			model.addAttribute("role", "Citizen Role must be valid");
			return false; 
		}
		if(citizen.getFirstName() == null || citizen.getFirstName().length() > 15
				||  citizen.getFirstName().length() < 1) {
			model.addAttribute("fname", "First Name must be valid");
			return false; 
		}
		if(citizen.getFirstName() == null || citizen.getFirstName().length() > 15
				||  citizen.getFirstName().length() < 1) {
			model.addAttribute("fname", "First Name must be valid");
			return false; 
		}
		if(citizen.getLastName() == null || citizen.getLastName().length() > 15
				||  citizen.getLastName().length() < 1) {
			model.addAttribute("lname", "Last Name must be valid");
			return false; 
		}
		validField = Pattern.compile("^(.+)@(.+)$").matcher(citizen.getEmail()).find();
		if(citizen.getEmail() == null || !validField) {
			model.addAttribute("email", "Email must be valid");
			return false;
		}
		validField = Pattern.compile("^[0-9]{9}$").matcher(citizen.getPhoneNumber()).find();
		if(!validField) {
			model.addAttribute("phoneNumber", "Phone number must be valid");
			return false;
		}
		if(citizen.getBirthday() == null) {
			model.addAttribute("birthday", "Birthday number must be valid");
			return false;
		}
		if(citizen.getCity() == null) {
			model.addAttribute("city", "City number must be valid");
			return false;
		}
		return true;
	}
}
