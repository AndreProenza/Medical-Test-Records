package com.hospital.utils;

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
	private int height;
	private int weight;
	private String bloodType;
	private int bloodPressure;
	private int bloodTemperature;
	private String diagnosis;
	private String date;
	
	
	public PatientRecord(String id, String firstName, String lastName, String email, String phoneNumber,
			String birthday, String city, int height, int weight, String bloodType, int bloodPressure,
			int bloodTemperature, String diagnosis, String date) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.city = city;
		this.height = height;
		this.weight = weight;
		this.bloodType = bloodType;
		this.bloodPressure = bloodPressure;
		this.bloodTemperature = bloodTemperature;
		this.diagnosis = diagnosis;
		this.date = date;
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


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
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


	public String getDiagnosis() {
		return diagnosis;
	}


	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
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
				+ ", phoneNumber=" + phoneNumber + ", birthday=" + birthday + ", city=" + city + ", height=" + height
				+ ", weight=" + weight + ", bloodType=" + bloodType + ", bloodPressure=" + bloodPressure
				+ ", bloodTemperature=" + bloodTemperature + ", diagnosis=" + diagnosis + ", date=" + date + "]";
	}
	
}
