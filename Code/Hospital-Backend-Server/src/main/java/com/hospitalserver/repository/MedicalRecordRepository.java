package com.hospitalserver.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospitalserver.model.mongodb.MedicalRecord;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {

	List<MedicalRecord> findByCid(String cid);

}