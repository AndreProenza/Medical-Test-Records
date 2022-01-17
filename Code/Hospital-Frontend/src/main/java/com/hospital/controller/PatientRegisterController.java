package com.hospital.controller;


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

import com.hospital.model.Citizen;
import com.hospital.utils.BackendUri;

@Controller
@RequestMapping("patient/register")
public class PatientRegisterController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Shows form and set form fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String showForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		model.addAttribute("message", "");
		return "patient_register";
	}
	
	
	/**
	 * Registers a patient
	 * Reads form, validate it and saves patient details in database
	 *
	 * @requires citizenId not registered in database
	 * @param citizen
	 * @param errors the form errors
	 * @param model the model
	 * @return the page to display
	 * @throws URISyntaxException 
	 */
	@PostMapping("")
	public String registerPatient(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors, Model model) 
			throws URISyntaxException {
		//If form has errors
		if(!(errors.getErrorCount() == 2 && errors.hasFieldErrors("role") && errors.hasFieldErrors("password"))) {
			return "patient_register";
		}
		
		if(isFormValid(citizen, model)) {
			
			URI uri = new URI(BackendUri.PATIENT_REGISTER_POST);
			Citizen savedCitizen = restTemplate.postForObject(uri, citizen, Citizen.class);
			
			//If citizen was not saved sucessfully
			if(savedCitizen == null) {
				model.addAttribute("message", "Error when registering patient!\nCitizen ID already registered\n");
				return "patient_register";
			}
			
			model.addAttribute("message", "Patient Details");
			return "register_success";	
		}
		else {
			model.addAttribute("message", "Error when registering patient!\nCitizen ID already registered\n");
			return "patient_register";						
		}
	}
	
	private boolean isFormValid(Citizen citizen, Model model) {
		boolean validField;
		
		validField = Pattern.compile("^[0-9]{8}$").matcher(citizen.getId()).find();
		if(!validField || citizen.getId() == null) {
			model.addAttribute("id", "Citizen id must be valid");
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
