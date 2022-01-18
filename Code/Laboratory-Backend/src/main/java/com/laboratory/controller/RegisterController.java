package com.laboratory.controller;

import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.service.CitizenService;
import com.laboratory.service.EmailSenderService;
import com.laboratory.utils.PasswordGenerator;

@RestController
@RequestMapping("admin/register")
public class RegisterController {

	private CitizenService citizenService;

	@Autowired
	private EmailSenderService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public RegisterController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}

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
	 * Registers a lab responsible Reads form, validate it and saves responsible
	 * details in database Generates a secure password and sends it to responsible
	 * email
	 *
	 * @requires citizenId not registered in database
	 * @param citizen the citizen
	 * @param errors  the form errors
	 * @param model   the model
	 * @return the page to display
	 */
	@PostMapping("")
	public String registerLabResponsible(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors,
			Model model) {
		// If form has errors
		if (!(errors.getErrorCount() == 1 && errors.hasFieldErrors("password"))) {
			return "register";
		}

		// Generate password
		String clearTextPassword = PasswordGenerator.generate(16);

		// creates the hash of the password used in the register
		String hashedPassword = bCryptPasswordEncoder.encode(clearTextPassword);
		
		citizen.setPassword(hashedPassword);
		if (isFormValid(citizen, model) && !citizenService.existsCitizenById(citizen.getId())) {
			citizenService.saveCitizen(citizen);

			// Send password details to citizen email
			emailService.sendLaboratoryEmail(citizen.getEmail(), citizen.getId(), clearTextPassword);
			model.addAttribute("message", "Details");
			return "register_success";
		} else {
			model.addAttribute("message", "Error when registering personnel!\nCitizen ID already registered\n");
			return "register";
		}
	}

	/**
	 * Check if field form are valid
	 * 
	 * @param record the clinical record
	 * @param model  the model
	 * @return true if all fields are correct, false otherwise
	 */
	private boolean isFormValid(Citizen citizen, Model model) {
		boolean validField;

		validField = Pattern.compile("^[0-9]{8}$").matcher(citizen.getId()).find();
		if (!validField || citizen.getId() == null) {
			model.addAttribute("id", "Citizen id must be valid");
			return false;
		}
		if (citizen.getRole() == null) {
			model.addAttribute("role", "Citizen Role must be valid");
			return false;
		}
		if (citizen.getFirstName() == null || citizen.getFirstName().length() > 15
				|| citizen.getFirstName().length() < 1) {
			model.addAttribute("fname", "First Name must be valid");
			return false;
		}
		if (citizen.getFirstName() == null || citizen.getFirstName().length() > 15
				|| citizen.getFirstName().length() < 1) {
			model.addAttribute("fname", "First Name must be valid");
			return false;
		}
		if (citizen.getLastName() == null || citizen.getLastName().length() > 15
				|| citizen.getLastName().length() < 1) {
			model.addAttribute("lname", "Last Name must be valid");
			return false;
		}
		validField = Pattern.compile("^(.+)@(.+)$").matcher(citizen.getEmail()).find();
		if (citizen.getEmail() == null || !validField) {
			model.addAttribute("email", "Email must be valid");
			return false;
		}
		validField = Pattern.compile("^[0-9]{9}$").matcher(citizen.getPhoneNumber()).find();
		if (!validField) {
			model.addAttribute("phoneNumber", "Phone number must be valid");
			return false;
		}
		if (citizen.getBirthday() == null) {
			model.addAttribute("birthday", "Birthday number must be valid");
			return false;
		}
		if (citizen.getCity() == null) {
			model.addAttribute("city", "City number must be valid");
			return false;
		}
		return true;
	}
}
