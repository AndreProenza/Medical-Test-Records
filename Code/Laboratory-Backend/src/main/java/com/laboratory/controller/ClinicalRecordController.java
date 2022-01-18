package com.laboratory.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laboratory.model.mongodb.Citizen;
import com.laboratory.model.mongodb.ClinicalRecord;
import com.laboratory.service.CitizenService;
import com.laboratory.service.ClinicalRecordService;
import com.laboratory.utils.PatientRecord;

@RestController
@RequestMapping("clinical/records")
public class ClinicalRecordController {
	
	private ClinicalRecordService clinicalRecordService;
	private CitizenService citizenService;

	public ClinicalRecordController(ClinicalRecordService clinicalRecordService, CitizenService citizenService) {
		super();
		this.clinicalRecordService = clinicalRecordService;
		this.citizenService = citizenService;
	}
	
	
	/**
	 * Loads all patient records from data base and set fields to model
	 * 
	 * @param model the model
	 * @return the page to display
	 */
	@GetMapping("")
	public String loadAllRecords(Model model) {		
		
		List<PatientRecord> records = getAllPatientsRecords();	
		ClinicalRecord record = new ClinicalRecord();
		
		model.addAttribute("records", records);
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "clinical_records";
	}
	
	
	/**
	 * Get all records from a specific patient
	 * 
	 * @param record the clinical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("/get")
	public String getCitizenRecords(@ModelAttribute("record") ClinicalRecord record, Model model) {
		List<ClinicalRecord> clinicalRecords = clinicalRecordService.getAllRecordsByCitizenId(record.getCid());
		if(clinicalRecords.isEmpty()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "clinical_records";
		}
		else {
			Citizen existingCitizen = citizenService.getCitizenById(record.getCid());
			List<PatientRecord> records = getAllPatientRecords(clinicalRecords, existingCitizen);
			model.addAttribute("records", records);
			model.addAttribute("citizen", existingCitizen);
			return "patient_record";			
		}
	}
	
	
	/**
	 * Get all patients records in database and saves them in a list of PatientRecord
	 * 
	 * @return the list of all records associated to each patient
	 */
	private List<PatientRecord> getAllPatientsRecords() {
		
		List<ClinicalRecord> clinicalRecords = clinicalRecordService.getAllRecords();
		List<Citizen> citizens = citizenService.getAllCitizens();
		List<PatientRecord> records = new ArrayList<>();
		
		for(ClinicalRecord clinicalRecord : clinicalRecords) {
			for(Citizen citizen : citizens) {
				if(clinicalRecord.getCid().equals(citizen.getId())) {
					PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
							citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
							citizen.getBirthday(), citizen.getCity(), clinicalRecord.getBloodType(),
							clinicalRecord.getBloodPressure(), clinicalRecord.getBloodTemperature(),
							clinicalRecord.getDate());
					records.add(patientRecord);
					break;
				}
			}
		}
		return records;
	}
	
	
	/**
	 * Get all patient records in database and saves them in a list of PatientRecord
	 * 
	 * @param clinicalRecords the list of clinical records
	 * @param citizen the citizen id
	 * @requires each clinical record citizen id must be equal to the citizen id 
	 * @return the list of all records associated to citizen
	 */
	private List<PatientRecord> getAllPatientRecords(List<ClinicalRecord> clinicalRecords, Citizen citizen) {
		
		List<PatientRecord> records = new ArrayList<>();
		
		for(ClinicalRecord clinicalRecord : clinicalRecords) {	
			PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
					citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
					citizen.getBirthday(), citizen.getCity(), clinicalRecord.getBloodType(),
					clinicalRecord.getBloodPressure(), clinicalRecord.getBloodTemperature(),
					clinicalRecord.getDate());
			records.add(patientRecord);
		}
		return records;
	}
	
	
	

	
	//---------------- POSTMAN --------------------//
	
	@PostMapping("/add")
	public ResponseEntity<ClinicalRecord> saveMedicalRecord(@RequestBody ClinicalRecord record) {
		return new ResponseEntity<ClinicalRecord>(clinicalRecordService.saveRecord(record), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ClinicalRecord>> getAllMedicalRecords() {
		return new ResponseEntity<>(clinicalRecordService.getAllRecords(), HttpStatus.OK);
	}
	
//	@GetMapping("/get/{id}")
//	public ResponseEntity<ClinicalRecord> getMedicalRecordById(@PathVariable("id") String id) {
//		return new ResponseEntity<ClinicalRecord>(clinicalRecordService.getRecordById(id), HttpStatus.OK);
//	}
	
	/* Only Doctors and Nurses can update medical records */
	@PutMapping("/update/{id}")
	public ResponseEntity<ClinicalRecord> updateMedicalRecord(@RequestBody ClinicalRecord record, 
			@PathVariable("id") String id) {
		return new ResponseEntity<ClinicalRecord>(
				clinicalRecordService.updateRecord(record, id), HttpStatus.OK);
	}

	
	/* Administrator can delete medical records*/	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<String> deleteMedicalRecord(@PathVariable("id") String id) {
		//Delete from Database
		clinicalRecordService.deleteRecord(id);
		return new ResponseEntity<String>("Medical record deleted Sucessfully", HttpStatus.OK);
	}
	
	//-------------------------------------------//
}
