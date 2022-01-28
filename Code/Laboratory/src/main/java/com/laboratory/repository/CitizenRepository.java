package com.laboratory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laboratory.model.mongodb.Citizen;

public interface CitizenRepository extends MongoRepository<Citizen, String> {

	Citizen findByIdAndPassword(String citizenId, String password);

}
