package com.hospital.security.accesspolicy;

import java.util.ArrayList;
import java.util.List;

final class Role {

    private static final String ADMIN = "Admin";
    private static final String DOCTOR = "Doctor";
    private static final String NURSE = "Nurse";
    private static final String PORTER = "Porter";
    private static final String VOLUNTEER = "Volunteer";
    private static final String PATIENT_ASSISTANT = "Patient_Assistant";
    private static final String CLINICAL_ASSISTANT = "Clinical_Assistant";
    private static final String WARD_CLERK = "Ward_Clerk";
    private static final String PATIENT = "Patient";

    static List<String> getAllRoles() { 
    	
    	List<String> roles = new ArrayList<>();
    	roles.add(ADMIN);
    	roles.add(DOCTOR);
    	roles.add(NURSE);
    	roles.add(PORTER);
    	roles.add(VOLUNTEER);
    	roles.add(PATIENT_ASSISTANT);
    	roles.add(CLINICAL_ASSISTANT);
    	roles.add(WARD_CLERK);
    	roles.add(PATIENT);
    	return roles;
    }
}
