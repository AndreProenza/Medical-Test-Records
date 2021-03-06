package com.hospital.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.MedicalRecordService;
import com.hospital.utils.PatientRecord;
import com.hospital.service.CitizenService;
import com.hospital.model.mongodb.Citizen;

@Controller
@RequestMapping("medical/records")
public class MedicalRecordController {
	
	private MedicalRecordService medicalRecordService;
	private CitizenService citizenService;

	public MedicalRecordController(MedicalRecordService medicalRecordService, CitizenService citizenService) {
		super();
		this.medicalRecordService = medicalRecordService;
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
		MedicalRecord record = new MedicalRecord();
		
		model.addAttribute("records", records);
		model.addAttribute("record", record);
		model.addAttribute("message", "");
		return "medical_records";
	}
	
	
	/**
	 * Get all records from a specific patient
	 * 
	 * @param record the medical record
	 * @param model model the model
	 * @return the page to display
	 */
	@GetMapping("/get")
	public String getCitizenRecords(@ModelAttribute("record") MedicalRecord record, Model model) {
		List<MedicalRecord> medicalRecords = medicalRecordService.getAllRecordsByCitizenId(record.getCid());
		if(medicalRecords.isEmpty()) {
			model.addAttribute("message", "Error when searching record!\nCitizen ID not registered\n");
			return "medical_records";
		}
		else {
			Citizen existingCitizen = citizenService.getCitizenById(record.getCid());
			List<PatientRecord> records = getAllPatientRecords(medicalRecords, existingCitizen);
			model.addAttribute("records", records);
			model.addAttribute("citizen", existingCitizen);
			return "patient_record";			
		}
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



	/**
	 * Get all patients records in database and saves them in a list of PatientRecord
	 * 
	 * @return the list of all records associated to each patient
	 */
	private List<PatientRecord> getAllPatientsRecords() {
		
		List<MedicalRecord> medicalRecords = medicalRecordService.getAllRecords();
		List<Citizen> citizens = citizenService.getAllCitizens();
		List<PatientRecord> records = new ArrayList<>();
		
		for(MedicalRecord medicalRecord : medicalRecords) {
			for(Citizen citizen : citizens) {
				if(medicalRecord.getCid().equals(citizen.getId())) {
					PatientRecord patientRecord = new PatientRecord(citizen.getId(), citizen.getFirstName(),
							citizen.getLastName(), citizen.getEmail(), citizen.getPhoneNumber(),
							citizen.getBirthday(), citizen.getCity(), medicalRecord.getHeight(),
							medicalRecord.getWeight(), medicalRecord.getBloodType(),
							medicalRecord.getBloodPressure(), medicalRecord.getBloodTemperature(),
							medicalRecord.getDiagnosis(), medicalRecord.getDate());
					records.add(patientRecord);
					break;
				}
			}
		}
		return records;
	}
	
	
	

	
	//---------------- POSTMAN --------------------//
	
	@PostMapping("/add")
	public ResponseEntity<MedicalRecord> saveMedicalRecord(@RequestBody MedicalRecord record) {
		return new ResponseEntity<MedicalRecord>(medicalRecordService.saveRecord(record), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
		return new ResponseEntity<>(medicalRecordService.getAllRecords(), HttpStatus.OK);
	}
	
//	@GetMapping("/get/{id}")
//	public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable("id") String id) {
//		return new ResponseEntity<MedicalRecord>(medicalRecordService.getRecordById(id), HttpStatus.OK);
//	}
	
	/* Only Doctors and Nurses can update medical records */
	@PutMapping("/update/{id}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord record, 
			@PathVariable("id") String id) {
		return new ResponseEntity<MedicalRecord>(
				medicalRecordService.updateRecord(record, id), HttpStatus.OK);
	}

	
	/* Administrator can delete medical records*/	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<String> deleteMedicalRecord(@PathVariable("id") String id) {
		//Delete from Database
		medicalRecordService.deleteRecord(id);
		return new ResponseEntity<String>("Medical record deleted Sucessfully", HttpStatus.OK);
	}
	
	//-------------------------------------------//
}
