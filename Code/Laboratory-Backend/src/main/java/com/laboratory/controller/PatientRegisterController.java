package com.laboratory.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.service.CitizenService;
import com.laboratory.utils.PasswordGenerator;

@RestController
@RequestMapping("api/patient/register")
public class PatientRegisterController {

	private CitizenService citizenService;

	public PatientRegisterController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Registers a patient, saves patient details in database
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

		return citizen;
	}

}
