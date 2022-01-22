package com.hospitalserver.service.implementation;

import org.springframework.stereotype.Service;

import com.hospitalserver.model.mongodb.MedicalRecord;
import com.hospitalserver.repository.MedicalRecordRepository;
import com.hospitalserver.service.MedicalRecordService;

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


}
