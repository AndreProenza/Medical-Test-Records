package com.hospital.utils;

/**
 * Backend api uris to consume backend rest api
 */
public class BackendUri {
	
	public final static String BACKEND_API_BASE_URI = "http://localhost:8080/";
	public final static String MEDICAL_RECORDS_ALL = "medical/records";
	public final static String MEDICAL_RECORDS_GET = "medical/records/get/{id}";
	public final static String CITIZEN_EXISTS_GET = "citizen/exists/{id}";
	public final static String CITIZEN_GET = "citizen/get/{id}";
	public final static String MY_MEDICAL_RECORDS_GET = "my/medical/record/get/{id}";
	public final static String MY_MEDICAL_RECORDS_DEFAULT_GET = "my/medical/record/default/{id}";
	public final static String MEDICAL_RECORD_POST = BACKEND_API_BASE_URI + "patient/medical/record/register";
	public final static String PATIENT_REGISTER_POST = BACKEND_API_BASE_URI + "patient/register";
	public final static String REGISTER_PERSONNEL_POST = BACKEND_API_BASE_URI + "admin/register";
}
