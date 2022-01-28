package com.hospital.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.hospital.model.Citizen;
import com.hospital.utils.BackendUri;
import com.hospital.utils.PatientRecord;

@Controller
@RequestMapping("my/medical/record")
public class MyMedicalRecordController {

	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Get all records from a specific citizen
	 * 
	 * @param record the medical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String getCitizenRecords(Model model, Authentication authentication) {
		model.addAttribute("message", "");
		
		String citizenId = authentication.getName();
		
		if(citizenId != null) {			

			List<PatientRecord> records = Arrays.asList(restTemplate.getForObject(BackendUri.MY_MEDICAL_RECORDS_GET, PatientRecord[].class, citizenId));
			
			Citizen existingCitizen = restTemplate.getForObject(BackendUri.CITIZEN_GET, Citizen.class, citizenId);
			
			if(records.isEmpty()) {

				PatientRecord record = restTemplate.getForObject(BackendUri.MY_MEDICAL_RECORDS_DEFAULT_GET, PatientRecord.class, citizenId);
				
				model.addAttribute("message", "You have no medical records yet!\n");
				model.addAttribute("records", record);
				model.addAttribute("citizen", existingCitizen);
				return "my_medical_record";
			}
			else {
				model.addAttribute("records", records);
				model.addAttribute("citizen", existingCitizen);
				return "my_medical_record";			
			}
		}
		return "error";
	}
}
