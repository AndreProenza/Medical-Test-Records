package com.hospital.service;

public interface EmailSenderService {
	
	/**
	 * Sends an email
	 * 
	 * @param toEmail the email to send the email
	 * @param subject the subject
	 * @param body the email body
	 */
	void sendEmail(String toEmail, String subject, String body);
	
	/**
	 * Sends an email with static subject and body
	 * 
	 * @param toEmail the email to send the email
	 * @param citizenId the citizen card id
	 * @param password the password
	 */
	void sendHospitalEmail(String toEmail,  String citizenId, String password);
}
