package com.hospitalserver.model.mongodb;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("record")
public class MedicalRecord {
	
	@NotNull(message = "Citizen ID cannot be null")
	@Pattern(regexp = ("^[0-9]{8}$"), message = "Citizen ID should have 8 numbers")
	private String cid;
	
	@Min(value = 50, message = "Height should not be less than 50cm")
    @Max(value = 230, message = "Height should not be greater than 230cm")
	private int height;
	
	@Min(value = 1, message = "Weight should not be less than 1kg")
    @Max(value = 300, message = "Weight should not be greater than 300kg")
	private int weight;
	
	@NotNull(message = "Blood type cannot be null")
	private String bloodType;
	
	@Min(value = 1, message = "Blood pressure should not be less than 1")
    @Max(value = 300, message = "Blood pressure should not be greater than 300")
	private int bloodPressure;
	
	@Min(value = 30, message = "Blood temperature should not be less than 30")
    @Max(value = 45, message = "Blood temperature should not be greater than 45")
	private int bloodTemperature;
	
	@NotNull(message = "Diagnosis cannot be null")
	@Size(min = 0, max = 50, message = "Diagnosis should not be greater than 50 characters")
	private String diagnosis;
	
	@NotNull(message = "Date cannot be null")
	@Size(min = 10, max = 10, message = "Date should be valid")
	private String date;
	
	
	public MedicalRecord(String cid, int height, int weight, String bloodType, int bloodPressure, 
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


	public MedicalRecord() {
		//Does Nothing
	}


	public String getCid() {
		return cid;
	}


	public void setCid(String cid) {
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


	@Override
	public String toString() {
		return "MedicalRecord [cid=" + cid + ", height=" + height + ", weight=" + weight + ", bloodType=" + bloodType
				+ ", bloodPressure=" + bloodPressure + ", bloodTemperature=" + bloodTemperature + ", diagnosis="
				+ diagnosis + ", date=" + date + "]";
	}
	
}

