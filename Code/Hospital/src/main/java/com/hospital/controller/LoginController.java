package com.hospital.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.tomcat.jni.Error;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.model.mongodb.Citizen;
import com.hospital.service.CitizenService;

@Controller
@RequestMapping("login")
public class LoginController {
	
	private CitizenService citizenService;
	
	public LoginController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}
	
	//---------- ALTERAR HARCODED -----------------//
	@GetMapping("")
	public String showLoginForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		return "login";
	}
	
	//---------- ALTERAR HARCODED -----------------//
	@PostMapping("")
	public String submitLoginForm(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors) {
		if(errors.hasErrors()) {
			return "login";
		}
	    //Check if exists
//	    Optional<Citizen> existingCitizen = Optional.ofNullable(citizenService.getCitizenById(citizen.getId()));
//	    if(existingCitizen.isPresent()) {
//	    	return "redirect:/home";
//	    }
	    return "login";
	}
}
