package com.hospitalserver.service.implementation;

import org.springframework.stereotype.Service;

import com.hospitalserver.repository.CitizenRepository;
import com.hospitalserver.service.CitizenService;

@Service
public class CitizenServiceImplementation implements CitizenService {
	
	
	private CitizenRepository citizenRepository;

	public CitizenServiceImplementation(CitizenRepository citizenRepository) {
		super();
		this.citizenRepository = citizenRepository;
	}

	@Override
	public boolean existsCitizenById(String id) {
		return citizenRepository.existsById(id);
	}

	
}
