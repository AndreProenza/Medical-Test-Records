package com.laboratory.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laboratory.model.mongodb.ClinicalRecord;

public interface ClinicalRecordRepository extends MongoRepository<ClinicalRecord, String> {

	List<ClinicalRecord> findByCid(String id);

}