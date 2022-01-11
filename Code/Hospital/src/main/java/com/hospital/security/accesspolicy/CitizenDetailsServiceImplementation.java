package com.hospital.security.accesspolicy;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hospital.model.mongodb.Citizen;
import com.hospital.repository.CitizenRepository;

public class CitizenDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	CitizenRepository citizenRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Citizen> citizen = citizenRepository.findById(username);
		if(!citizen.isPresent()) {
			throw new UsernameNotFoundException("Could not find Citizen");
		}
		return new CitizenDetails(citizen.get());
	}

}
