package com.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.model.mongodb.Citizen;

public interface CitizenRepository extends MongoRepository<Citizen, Integer> {

}
