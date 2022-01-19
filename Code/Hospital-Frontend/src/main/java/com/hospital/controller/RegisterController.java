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
@RequestMapping("admin/register")
public class RegisterController {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Shows form and set form fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String showRegisterForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		model.addAttribute("message", "");
		return "register";
	}
	
	/**
	 * Registers a personnel citizen
	 * Reads form, validate it and saves citizen details in database
	 * Generates a secure password and sends it to responsible email
	 *
	 * @requires citizenId not registered in database
	 * @param citizen the citizen
	 * @param errors the form errors
	 * @param model the model
	 * @return the page to display
	 * @throws URISyntaxException 
	 */
	@PostMapping("")
	public String registerPersonnel(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors, Model model) 
			throws URISyntaxException {
		//If form has errors
		if(!(errors.getErrorCount() == 1 && errors.hasFieldErrors("password"))) {
			return "register";
		}
		
		if(isFormValid(citizen, model)) {
			
			URI uri = new URI(BackendUri.REGISTER_PERSONNEL_POST);
			Citizen savedCitizen = restTemplate.postForObject(uri, citizen, Citizen.class);
			
			//If citizen was not saved successfully. If savedCitizen is null, citizen already exists in database
			if(savedCitizen == null) {
				model.addAttribute("message", "Error when registering personnel!\nCitizen ID already registered\n");
				return "register";
			}
			
			model.addAttribute("message", "Details");
			return "register_success";
		}
		else {
			model.addAttribute("message", "Error when registering personnel!\nCitizen ID already registered\n");
			return "register";
		}		
	}
	
	/**
	 * Check if field form are valid
	 * 
	 * @param record the clinical record
	 * @param model the model
	 * @return true if all fields are correct, false otherwise
	 */
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
