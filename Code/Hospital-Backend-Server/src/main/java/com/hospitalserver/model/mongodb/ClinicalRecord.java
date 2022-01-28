package com.hospitalserver.model.mongodb;

import java.io.Serializable;

public class ClinicalRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5271002436696745005L;

	private String cid;
	
	private String bloodType;
	
	private int bloodPressure;
	
	private int bloodTemperature;
	
	private String date;
	
	
	/*public ClinicalRecord(String cid, int height, int weight, String bloodType, int bloodPressure, 
			int bloodTemperature, String diagnosis, String date) {
		super();
		this.cid = cid;
		this.bloodType = bloodType;
		this.bloodPressure = bloodPressure;
		this.bloodTemperature = bloodTemperature;
		this.date = date;
	}*/

	public ClinicalRecord(String cid, String bloodType, int bloodPressure, 
			int bloodTemperature, String date) {
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

