package com.laboratory.service;

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
	void sendLaboratoryEmail(String toEmail,  String citizenId, String password);
	
	/**
	 * Sends an email with static subject and body informing the citizen, that
	 * password has been changed
	 * 
	 * @param toEmail the email to send the email
	 * @param citizenId the citizen card id
	 */
	void sendLaboratoryChangePasswordEmail(String toEmail);
}
