package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.mongodb.Citizen;
import com.hospital.service.CitizenService;
import com.hospital.service.EmailSenderService;
import com.hospital.utils.PasswordGenerator;

@RestController
@RequestMapping("api/patient/register")
public class PatientRegisterController {

	private CitizenService citizenService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailSenderService emailService;
	
	public PatientRegisterController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	
	/**
	 * Registers a patient, saves patient details in database and sends email to patient email
	 *
	 * @requires citizenId not registered in database
	 * @param citizen the citizen to save
	 * @return citizen if it is correctly saved in database, null otherwise.
	 */
	@PostMapping("")
	public Citizen registerPatient(@RequestBody Citizen citizen) {
		
		if(citizenService.existsCitizenById(citizen.getId())) {
			return null;
		}
		
		// Generate password
		String clearTextPassword = PasswordGenerator.generate(16);

		// creates the hash of the password used in the register
		String hashedPassword = bCryptPasswordEncoder.encode(clearTextPassword);
		
		citizen.setPassword(hashedPassword);
	
		//Attribute Role to citizen
		citizen.setRole("Patient");

		citizenService.saveCitizen(citizen);
		
		//Send password details to citizen email
		emailService.sendHospitalEmail(citizen.getEmail(), citizen.getId(), clearTextPassword);
		return citizen;
	}
}
