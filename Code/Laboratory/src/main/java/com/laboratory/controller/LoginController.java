package com.laboratory.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.laboratory.model.mongodb.Citizen;
import com.laboratory.service.CitizenService;
import com.laboratory.utils.Hasher;

@Controller
@RequestMapping("login")
public class LoginController {

	private CitizenService citizenService;

	public LoginController(CitizenService citizenService) {
		super();
		this.citizenService = citizenService;
	}

	@GetMapping("")
	public String showLoginForm(Model model) {
		Citizen citizen = new Citizen();
		model.addAttribute("citizen", citizen);
		return "login";
	}

	@PostMapping("")
	public String submitLoginForm(@ModelAttribute("citizen") @Valid Citizen citizen, Errors errors) {
		if (errors.hasFieldErrors("id") && errors.hasFieldErrors("password")) {
			return "login";
		}
		// creates the hash of the password used in the attempt
		String hashedPassword = Hasher.hash(citizen.getPassword());

		Citizen existingCitizen = citizenService.login(citizen.getId(), hashedPassword);
		return existingCitizen == null ? "login" : "home";
	}
}
