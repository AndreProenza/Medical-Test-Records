package com.laboratory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.service.CitizenService;

@RestController
@RequestMapping("api/citizen")
public class CitizenController {
	
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

}
