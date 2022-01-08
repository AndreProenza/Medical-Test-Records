package com.hospital.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class HomeController {
	
	@GetMapping("")
	public String loadHomePage() {
		return "home";
	}
}
