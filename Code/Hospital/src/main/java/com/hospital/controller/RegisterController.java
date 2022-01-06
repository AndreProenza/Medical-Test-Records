package com.hospital.controller;

import java.util.Optional;

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

@Controller
@RequestMapping("api/register")
public class RegisterController {
	
private CitizenService citizenService;
	
	public RegisterController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	
	@GetMapping("")
	public String showRegisterForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		return "register";
	}
	
	//TODO
	@PostMapping("")
	public String submitRegisterForm(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors) {
		if(errors.hasErrors()) {
			return "register";
		}
		System.out.println(citizen);
		//Check if username and citizen ID already exists in database
//	    Optional<Citizen> existingCitizen = Optional.ofNullable(citizenService.getCitizenById("16337575"));
//	    if(existingCitizen.isPresent()) {
//	    	return "redirect:/home";
//	    }
	    return "register";
	}
}
