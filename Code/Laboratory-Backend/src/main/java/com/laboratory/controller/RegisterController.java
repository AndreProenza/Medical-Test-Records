package com.laboratory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.service.CitizenService;
import com.laboratory.service.EmailSenderService;
import com.laboratory.utils.PasswordGenerator;

@RestController
@RequestMapping("api/admin/register")
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
	public Citizen registerPersonnel(@RequestBody Citizen citizen) {
		
		if(citizenService.existsCitizenById(citizen.getId())) {
			return null;
		}
		// Generate password
		String clearTextPassword = PasswordGenerator.generate(16);

		// creates the hash of the password used in the register
		String hashedPassword = bCryptPasswordEncoder.encode(clearTextPassword);
		
		citizen.setPassword(hashedPassword);
		
		citizenService.saveCitizen(citizen);
		
		// Send password details to citizen email
		emailService.sendLaboratoryEmail(citizen.getEmail(), citizen.getId(), clearTextPassword);
		
		return citizen;
	}
	
}
