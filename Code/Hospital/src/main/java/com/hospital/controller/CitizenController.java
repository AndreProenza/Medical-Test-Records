package com.hospital.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.model.mongodb.Citizen;
import com.hospital.service.CitizenService;

@Controller
@RequestMapping("api/citizen")
public class CitizenController {
	
	private CitizenService citizenService;
	
	public CitizenController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<Citizen> saveCitizen(@RequestBody Citizen citizen) {
		return new ResponseEntity<Citizen>(citizenService.saveCitizen(citizen), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Citizen>> getAllCitizens() {
		return new ResponseEntity<>(citizenService.getAllCitizens(), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Citizen> getCitizenById(@PathVariable("id") int id) {
		return new ResponseEntity<Citizen>(citizenService.getCitizenById(id), HttpStatus.OK);
	}
	
	/* Ward clerks (staff the ward reception desks)*/
	@PutMapping("/update/{id}")
	public ResponseEntity<Citizen> updateCitizenAsWardClerk(@RequestBody Citizen citizen, @PathVariable("id") int id) {
		return new ResponseEntity<Citizen>(
				citizenService.updateCitizenAsWardClerk(citizen, id), HttpStatus.OK);
	}
	
	/* Administrator can modify citizen roles*/	
	@PutMapping("/admin/update/{id}")
	public ResponseEntity<Citizen> updateCitizenAsAdministrator(@RequestBody Citizen citizen, @PathVariable("id") int id) {
		return new ResponseEntity<Citizen>(
				citizenService.updateCitizenAsAdministrator(citizen, id), HttpStatus.OK);
	}
	
	/* Administrator can delete citizen*/	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<String> deleteCitizen(@PathVariable("id") int id) {
		//Delete from Database
		citizenService.deleteCitizen(id);
		return new ResponseEntity<String>("Citizen deleted Sucessfully", HttpStatus.OK);
	}


	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
