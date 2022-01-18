package com.laboratory.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.laboratory.exception.ResourceNotFoundException;
import com.laboratory.model.mongodb.ClinicalRecord;
import com.laboratory.repository.ClinicalRecordRepository;
import com.laboratory.service.ClinicalRecordService;

@Service
public class ClinicalRecordsImplementation implements ClinicalRecordService {
	
	private ClinicalRecordRepository clinicalRecordRepository;
	
	public ClinicalRecordsImplementation(ClinicalRecordRepository clinicalRecordRepository) {
		super();
		this.clinicalRecordRepository = clinicalRecordRepository;
	}

	@Override
	public ClinicalRecord saveRecord(ClinicalRecord clinicalRecord) {
		return clinicalRecordRepository.save(clinicalRecord);
	}

	@Override
	public List<ClinicalRecord> getAllRecords() {
		return clinicalRecordRepository.findAll();
	}

	@Override
	public ClinicalRecord getRecordById(String id) {
		Optional<ClinicalRecord> record = clinicalRecordRepository.findById(id);
		if(record.isPresent()) {
			return record.get();
		}
		throw new ResourceNotFoundException("clinical Record", "ID", id);
	}

	@Override
	public ClinicalRecord updateRecord(ClinicalRecord clinicalRecord, String id) {
		ClinicalRecord existingRecord = clinicalRecordRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("clinical Record", "ID", id));
		existingRecord.setCid(clinicalRecord.getCid());
		existingRecord.setBloodType(clinicalRecord.getBloodType());
		existingRecord.setBloodPressure(clinicalRecord.getBloodPressure());
		existingRecord.setBloodTemperature(clinicalRecord.getBloodTemperature());
		existingRecord.setDate(clinicalRecord.getDate());
		//Save in Database
		clinicalRecordRepository.save(existingRecord);
		return existingRecord;
	}

	@Override
	public void deleteRecord(String id) {
		clinicalRecordRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("clinical Record", "ID", id));
		clinicalRecordRepository.deleteById(id);
	}

	@Override
	public List<ClinicalRecord> getAllRecordsByCitizenId(String id) {
		return clinicalRecordRepository.findByCid(id);
	}

}
