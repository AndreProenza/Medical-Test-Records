package com.laboratory.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Citizen {
	
	@NotNull(message = "Citizen ID cannot be null")
	@Pattern(regexp = ("^[0-9]{8}$"), message = "Citizen ID should have 8 numbers")
	private String id;
	
	/*
	@Pattern(regexp = ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"), 
			message = "Password should contain at least eight characters, one uppercase letter, "
					+ "one lowercase letter, one number and one special character")
	*/ 
	
	@NotNull(message = "Password cannot be null")
	@Pattern(regexp = ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"), 
	message = "Incorrect citizen id and password combination")
	private String password;
	
	@NotNull(message = "Citizen role cannot be null")
	private String role;
	
	@NotNull(message = "First name cannot be null")
	@Size(min = 1, max = 15, message = "First name should not be greater than 15 characters")
	private String firstName;
	
	@NotNull(message = "Last name cannot be null")
	@Size(min = 1, max = 15, message = "Last name should not be greater than 15 characters")
	private String lastName;
	
	@NotNull(message = "Email cannot be null")
	@Pattern(regexp = "^(.+)@(.+)$", message = "Email should be valid")
	@Email(message = "Email should be valid")
	private String email;

	@NotNull(message = "Phone number cannot be null")
	@Pattern(regexp = "^[0-9]{9}$", message = "Phone number should have 9 digits")
	private String phoneNumber;
	
	@NotNull(message = "Birthday cannot be null")
	private String birthday;
	
	@NotNull(message = "City cannot be null")
	@Size(min = 1, max = 15, message = "City name should not be greater than 15 characters")
	private String city;
	
	public Citizen(String id, String password, String role, String firstName, String lastName, String email,
			String phoneNumber, String birthday, String city) {
		super();
		this.id = id;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.city = city;
	}

	public Citizen() {
		//Does Nothing
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String fName) {
		this.firstName = fName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lName) {
		this.lastName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Citizen [id=" + id + ", password=" + password + ", role=" + role
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", birthday=" + birthday + ", city=" + city + "]";
	}
	
	
}

