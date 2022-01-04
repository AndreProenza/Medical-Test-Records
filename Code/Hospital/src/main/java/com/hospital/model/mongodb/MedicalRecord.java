package com.hospital.model.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("record")
public class MedicalRecord {
	
	private int cid;
	private int height;
	private int weight;
	private String bloodType;
	private int bloodPressure;
	private int bloodTemperature;
	private String diagnosis;
	private String date;
	
	
	public MedicalRecord(int cid, int height, int weight, String bloodType, int bloodPressure, 
			int bloodTemperature, String diagnosis, String date) {
		super();
		this.cid = cid;
		this.height = height;
		this.weight = weight;
		this.bloodType = bloodType;
		this.bloodPressure = bloodPressure;
		this.bloodTemperature = bloodTemperature;
		this.diagnosis = diagnosis;
		this.date = date;
	}


	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
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
	
}

