package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.mongodb.Citizen;
import com.hospital.model.mongodb.CitizenCredentials;
import com.hospital.service.CitizenService;
import com.hospital.service.EmailSenderService;

@RestController
@RequestMapping("api/citizen")
public class CitizenController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailSenderService emailService;
	
	private CitizenService citizenService;
	
	public CitizenController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	@GetMapping("/get/{id}")
	public Citizen getCitizen(@PathVariable("id") String citizenId) {
		return citizenService.getCitizenById(citizenId);
	}
	
	@GetMapping("/exists/{id}")
	public Boolean existsCitizen(@PathVariable("id") String citizenId) {
		return Boolean.valueOf(citizenService.existsCitizenById(citizenId));
	}
	
	
	@PostMapping("/change/password")
	public CitizenCredentials changeCitizenPassword(@RequestBody CitizenCredentials citizenCredentials) {
		
		//Check if new password matches confirmation password
		if(!citizenCredentials.getNewPassword().equals(citizenCredentials.getConfPassword())) {
			return null;
		}
		
		String citizenId = citizenCredentials.getCitizenId();
		if(!citizenService.existsCitizenById(citizenId)) {
			return null;
		}
		Citizen citizen = citizenService.getCitizenById(citizenId);
		//Get password hashed in database
		String encodedPassword = citizen.getPassword();
		//Get raw password sent by the user
		String rawPassword = citizenCredentials.getOldPassword();
		
		//Check if password stored in database is equal to old password
		if(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
			
			// creates the hash of new password
			String hashedPassword = bCryptPasswordEncoder.encode(citizenCredentials.getNewPassword());
			
			//Set new citizen password in database
			citizen.setPassword(hashedPassword);
			citizenService.saveCitizen(citizen);
			
			//Send password details to citizen email
			emailService.sendHospitalChangePasswordEmail(citizen.getEmail());
			
			return citizenCredentials;
		}
		return null;
	}

}
