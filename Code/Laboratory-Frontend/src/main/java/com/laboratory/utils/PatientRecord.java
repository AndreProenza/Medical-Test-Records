package com.laboratory.utils;

/**
 * This class represents a patient
 * An instance of this class has all clinical records associated to the patient id (citizen id)
 */
public class PatientRecord {
	
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String birthday;
	private String city;
	private String bloodType;
	private int bloodPressure;
	private int bloodTemperature;
	private String date;
	
	
	public PatientRecord(String id, String firstName, String lastName, String email, String phoneNumber,
			String birthday, String city, String bloodType, int bloodPressure, int bloodTemperature, String date) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.city = city;
		this.bloodType = bloodType;
		this.bloodPressure = bloodPressure;
		this.bloodTemperature = bloodTemperature;
		this.date = date;
	}
	
	public PatientRecord() {
		
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public int getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(int bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public int getBloodTemperature() {
		return bloodTemperature;
	}

	public void setBloodTemperature(int bloodTemperature) {
		this.bloodTemperature = bloodTemperature;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "PatientRecord [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", birthday=" + birthday + ", city=" + city + ", bloodType="
				+ bloodType + ", bloodPressure=" + bloodPressure + ", bloodTemperature=" + bloodTemperature + ", date="
				+ date + "]";
	}
}
