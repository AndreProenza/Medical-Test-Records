package com.hospital.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.mongodb.MedicalRecord;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.service.MedicalRecordService;

@Service
public class MedicalRecordsImplementation implements MedicalRecordService {
	
	private MedicalRecordRepository medicalRecordRepository;
	
	public MedicalRecordsImplementation(MedicalRecordRepository medicalRecordRepository) {
		super();
		this.medicalRecordRepository = medicalRecordRepository;
	}

	@Override
	public MedicalRecord saveRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.save(medicalRecord);
	}

	@Override
	public List<MedicalRecord> getAllRecords() {
		return medicalRecordRepository.findAll();
	}

	@Override
	public MedicalRecord getRecordById(String id) {
		Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
		if(record.isPresent()) {
			return record.get();
		}
		throw new ResourceNotFoundException("Medical Record", "ID", id);
	}

	@Override
	public MedicalRecord updateRecord(MedicalRecord medicalRecord, String id) {
		MedicalRecord existingRecord = medicalRecordRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Medical Record", "ID", id));
		existingRecord.setCid(medicalRecord.getCid());
		existingRecord.setHeight(medicalRecord.getHeight());
		existingRecord.setWeight(medicalRecord.getWeight());
		existingRecord.setBloodType(medicalRecord.getBloodType());
		existingRecord.setBloodPressure(medicalRecord.getBloodPressure());
		existingRecord.setBloodTemperature(medicalRecord.getBloodTemperature());
		existingRecord.setDiagnosis(medicalRecord.getDiagnosis());
		existingRecord.setDate(medicalRecord.getDate());
		//Save in Database
		medicalRecordRepository.save(existingRecord);
		return existingRecord;
	}

	@Override
	public void deleteRecord(String id) {
		medicalRecordRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Medical Record", "ID", id));
		medicalRecordRepository.deleteById(id);
	}

	@Override
	public List<MedicalRecord> getAllRecordsByCitizenId(String cid) {
		return medicalRecordRepository.findByCid(cid);
	}

}
