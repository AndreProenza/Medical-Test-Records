package com.hospital.security.accesspolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hospital.model.mongodb.Citizen;

public class CitizenDetails implements UserDetails {
	
	private Citizen citizen;
	
	public CitizenDetails(Citizen citizen) {
		super();
		this.citizen = citizen;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(citizen.getRole()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return citizen.getPassword();
	}

	@Override
	public String getUsername() {
		return citizen.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
