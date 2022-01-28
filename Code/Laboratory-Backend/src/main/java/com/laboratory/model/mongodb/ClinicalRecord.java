package com.laboratory.model.mongodb;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("record")
public class ClinicalRecord {
	
	@NotNull(message = "Citizen ID cannot be null")
	@Pattern(regexp = ("^[0-9]{8}$"), message = "Citizen ID should have 8 numbers")
	private String cid;
	
	@NotNull(message = "Blood type cannot be null")
	private String bloodType;
	
	@Min(value = 1, message = "Blood pressure should not be less than 1")
    @Max(value = 300, message = "Blood pressure should not be greater than 300")
	private int bloodPressure;
	
	@Min(value = 30, message = "Body temperature should not be less than 30")
    @Max(value = 45, message = "Body temperature should not be greater than 45")
	private int bloodTemperature;
	
	@NotNull(message = "Date cannot be null")
	@Size(min = 10, max = 10, message = "Date should be valid")
	private String date;
	
	
	public ClinicalRecord(String cid, int height, int weight, String bloodType, int bloodPressure, 
			int bloodTemperature, String diagnosis, String date) {
		super();
		this.cid = cid;
		this.bloodType = bloodType;
		this.bloodPressure = bloodPressure;
		this.bloodTemperature = bloodTemperature;
		this.date = date;
	}
	



	public ClinicalRecord() {
		//Does Nothing
	}


	public String getCid() {
		return cid;
	}


	public void setCid(String cid) {
		this.cid = cid;
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


	@Override
	public String toString() {
		return "ClinicalRecord [cid=" + cid + ", bloodType=" + bloodType + ", bloodPressure=" + bloodPressure
				+ ", bloodTemperature=" + bloodTemperature + ", date=" + date + "]";
	}

}

