package com.laboratory.security.accesspolicy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import com.laboratory.model.Citizen;
import com.laboratory.utils.BackendUri;

public class CitizenDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Boolean existsCitizen = restTemplate.getForObject(BackendUri.CITIZEN_EXISTS_GET, Boolean.class, username);
//		System.out.println("existsCitizen: " + existsCitizen);
		if(!existsCitizen.booleanValue()) {
			throw new UsernameNotFoundException("Could not find Citizen");
		}
		
		Citizen citizen = restTemplate.getForObject(BackendUri.CITIZEN_GET, Citizen.class, username);
//		System.out.println("citizen: " + citizen);
		if(citizen == null) {
			throw new UsernameNotFoundException("Could not find Citizen");
		}
		return new CitizenDetails(citizen);
	}

}
