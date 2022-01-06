package com.hospital.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.service.MedicalRecordService;

@Controller
@RequestMapping("api/medical/records")
public class MedicalRecordController {
	
	private MedicalRecordService medicalRecordService;

	public MedicalRecordController(MedicalRecordService medicalRecordService) {
		super();
		this.medicalRecordService = medicalRecordService;
	}
	
	
	@GetMapping("")
	public String loadAllRecords(Model model) {
		List<MedicalRecord> records = medicalRecordService.getAllRecords();
		model.addAttribute("records", records);
		return "medical_records";
	}
	
	//----------- NOT WORKING ------------//
	
	@GetMapping("/get")
	public String getCitizenRecords(Model model, @RequestParam(name = "cid") String cid) {
		System.out.println("CID = " + cid);
		List<MedicalRecord> records = medicalRecordService.getAllRecords();
		model.addAttribute("records", records);
		return "patient";
	}
	
	//------------------------------------//
	
	
	

	
	
	
	@PostMapping("/add")
	public ResponseEntity<MedicalRecord> saveMedicalRecord(@RequestBody MedicalRecord record) {
		return new ResponseEntity<MedicalRecord>(medicalRecordService.saveRecord(record), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
		return new ResponseEntity<>(medicalRecordService.getAllRecords(), HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable("id") String id) {
		return new ResponseEntity<MedicalRecord>(medicalRecordService.getRecordById(id), HttpStatus.OK);
	}
	
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

}
