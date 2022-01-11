package com.hospital.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.model.mongodb.Citizen;
import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.CitizenService;
import com.hospital.service.MedicalRecordService;
import com.hospital.utils.PatientRecord;

@Controller
@RequestMapping("my/medical/record")
public class MyMedicalRecordController {

	private MedicalRecordService medicalRecordService;
	private CitizenService citizenService;
	
	
	public MyMedicalRecordController(MedicalRecordService medicalRecordService, CitizenService citizenService) {
		super();
		this.medicalRecordService = medicalRecordService;
		this.citizenService = citizenService;
	}
	
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
			
			List<MedicalRecord> medicalRecords = medicalRecordService.getAllRecordsByCitizenId(citizenId);
			Citizen existingCitizen = citizenService.getCitizenById(citizenId);
			
			if(medicalRecords.isEmpty()) {
				model.addAttribute("message", "You have no medical records yet!\n");
				model.addAttribute("records", getDefaultNullPatientRecord(existingCitizen));
				model.addAttribute("citizen", existingCitizen);
				return "my_medical_record";
			}
			else {
				List<PatientRecord> records = getAllPatientRecords(medicalRecords, existingCitizen);
				model.addAttribute("records", records);
				model.addAttribute("citizen", existingCitizen);
				return "my_medical_record";			
			}
		}
		return "error";
	}
	
	/**
	 * Get all patient records in database and saves them in a list of PatientRecord
	 * 
	 * @param medicalRecord the list of medical records
	 * @param citizen the citizen id
	 * @requires each medical record citizen id must be equal to the citizen id 
	 * @return the list of all records associated to citizen
	 */
	private List<PatientRecord> getAllPatientRecords(List<MedicalRecord> medicalRecords, Citizen citizen) {
		
		List<PatientRecord> records = new ArrayList<>();
		
		for(MedicalRecord medicalRecord : medicalRecords) {	
			PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
					citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
					citizen.getBirthday(), citizen.getCity(), medicalRecord.getHeight(),
					medicalRecord.getWeight(), medicalRecord.getBloodType(),
					medicalRecord.getBloodPressure(), medicalRecord.getBloodTemperature(),
					medicalRecord.getDiagnosis(), medicalRecord.getDate());
			records.add(patientRecord);
		}
		return records;
	}
	
	private PatientRecord getDefaultNullPatientRecord(Citizen citizen) {
		PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
				citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(), 
				citizen.getBirthday(), citizen.getCity(), 0, 0, "null", 0, 0, "null", "null");
		return patientRecord;
	}
}
