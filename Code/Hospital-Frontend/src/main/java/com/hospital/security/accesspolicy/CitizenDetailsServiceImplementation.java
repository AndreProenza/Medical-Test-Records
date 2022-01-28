package com.hospital.security.accesspolicy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import com.hospital.model.Citizen;

public class CitizenDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String uri = "citizen/exists/{id}";
		Boolean existsCitizen = restTemplate.getForObject(uri, Boolean.class, username);
		if(!existsCitizen.booleanValue()) {
			throw new UsernameNotFoundException("Could not find Citizen");
		}
		
		uri = "citizen/get/{id}";
		Citizen citizen = restTemplate.getForObject(uri, Citizen.class, username);
		if(citizen == null) {
			throw new UsernameNotFoundException("Could not find Citizen");
		}
		return new CitizenDetails(citizen);
	}

}
