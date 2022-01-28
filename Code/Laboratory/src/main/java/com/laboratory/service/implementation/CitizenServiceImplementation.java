package com.laboratory.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.laboratory.exception.ResourceNotFoundException;
import com.laboratory.model.mongodb.Citizen;
import com.laboratory.repository.CitizenRepository;
import com.laboratory.service.CitizenService;

@Service
public class CitizenServiceImplementation implements CitizenService {
	
	
	private CitizenRepository citizenRepository;

	public CitizenServiceImplementation(CitizenRepository citizenRepository) {
		super();
		this.citizenRepository = citizenRepository;
	}

	@Override
	public Citizen saveCitizen(Citizen citizen) {
		return citizenRepository.save(citizen);
	}

	@Override
	public List<Citizen> getAllCitizens() {
		return citizenRepository.findAll();
		
		
	}

	@Override
	public Citizen getCitizenById(String id) {
		Optional<Citizen> citizen = citizenRepository.findById(id);
		if(citizen.isPresent()) {
			return citizen.get();
		}
		throw new ResourceNotFoundException("Citizen", "Citizen ID", id);
	}

	
	/* Ward clerks (staff the ward reception desks)*/
	@Override
	public Citizen updateCitizenAsWardClerk(Citizen citizen, String id) {
		Citizen existingCitizen = citizenRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Citizen", "Citizen ID", id));
		existingCitizen.setFirstName(citizen.getFirstName());
		existingCitizen.setLastName(citizen.getFirstName());
		existingCitizen.setEmail(citizen.getEmail());
		existingCitizen.setPhoneNumber(citizen.getPhoneNumber());
		existingCitizen.setCity(citizen.getCity());
		existingCitizen.setBirthday(citizen.getBirthday());
		//Save in Database
		citizenRepository.save(existingCitizen);
		return existingCitizen;
	}
	
	/* Administrator can modify citizen roles*/	
	@Override
	public Citizen updateCitizenAsAdministrator(Citizen citizen, String id) {
		Citizen existingCitizen = citizenRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Citizen", "Citizen ID", id));
		
		existingCitizen.setFirstName(citizen.getFirstName());
		existingCitizen.setLastName(citizen.getFirstName());
		existingCitizen.setRole(citizen.getRole());
		existingCitizen.setEmail(citizen.getEmail());
		existingCitizen.setPhoneNumber(citizen.getPhoneNumber());
		existingCitizen.setCity(citizen.getCity());
		existingCitizen.setBirthday(citizen.getBirthday());
		//Save in Database
		citizenRepository.save(existingCitizen);
		return existingCitizen;
	}

	@Override
	public void deleteCitizen(String id) {
		citizenRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Citizen", "Citizen ID", id));
		citizenRepository.deleteById(id);
		
	}

	@Override
	public boolean existsCitizenById(String id) {
		return citizenRepository.existsById(id);
	}

	@Override
	public Citizen login(String citizenId, String password) {
		Citizen citizen = citizenRepository.findByIdAndPassword(citizenId, password);
		return citizen == null ? null : citizen;
	}
	
}
