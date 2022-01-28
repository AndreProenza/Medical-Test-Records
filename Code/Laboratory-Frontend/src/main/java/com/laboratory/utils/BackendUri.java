package com.laboratory.utils;

/**
 * Backend api uris to consume backend rest api
 */
public class BackendUri {
	
	public final static String BACKEND_API_BASE_URI = "https://localhost:3000/api/";
	public final static String CLINICAL_RECORDS_ALL = "clinical/records";
	public final static String CLINICAL_RECORDS_GET = "clinical/records/get/{id}";
	public final static String CITIZEN_EXISTS_GET = "citizen/exists/{id}";
	public final static String CITIZEN_GET = "citizen/get/{id}";
	public final static String CLINICAL_RECORD_POST = BACKEND_API_BASE_URI + "patient/clinical/record/register";
	public final static String PATIENT_REGISTER_POST = BACKEND_API_BASE_URI + "patient/register";
	public final static String REGISTER_PERSONNEL_POST = BACKEND_API_BASE_URI + "admin/register";
	public final static String CHANGE_PASSWORD_POST = BACKEND_API_BASE_URI + "citizen/change/password";
}
