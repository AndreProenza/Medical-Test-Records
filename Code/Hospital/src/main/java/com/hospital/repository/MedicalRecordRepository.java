package com.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.model.mongodb.MedicalRecord;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {

}