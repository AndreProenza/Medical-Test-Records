package com.laboratory.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.laboratory.model.CitizenCredentials;
import com.laboratory.utils.BackendUri;

@Controller
@RequestMapping("change/password")
public class PasswordController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	/**
	 * Shows form and set form fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String showForm(Model model) {
		CitizenCredentials password = new CitizenCredentials();
		model.addAttribute("password", password);
		model.addAttribute("message", "");
		model.addAttribute("success", "");
		return "password";
	}
	
	@PostMapping("")
	public String changePassoword(@ModelAttribute("password") @Valid CitizenCredentials citizenCredentials, Errors errors, Model model,
			Authentication authentication) 
			throws URISyntaxException {	
		
		citizenCredentials.setCitizenId(authentication.getName());
		
		if(!errors.hasFieldErrors("citizenId")) {
			return "password";
		}
		
		if(!citizenCredentials.getNewPassword().equals(citizenCredentials.getConfPassword())) {
			model.addAttribute("message", "New password and confirmation password don't match");
			return "password";
		}
		
		URI uri = new URI(BackendUri.CHANGE_PASSWORD_POST);
		CitizenCredentials updatedPassword = restTemplate.postForObject(uri, citizenCredentials, CitizenCredentials.class);
		
		//If password was not saved sucessfully
		if(updatedPassword == null) {
			model.addAttribute("message", "Old password is incorrect or new passwords don't match.");
			return "password";
		}
		else {
			return "password_success";
		}	
	}
}
