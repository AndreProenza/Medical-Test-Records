package com.laboratory.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.laboratory.service.EmailSenderService;

@Service
public class EmailSenderServiceImplementation implements EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setFrom("hospital.medical.records@gmail.com");
		mail.setTo(toEmail);
		mail.setSubject(subject);
		mail.setText(body);
		
		mailSender.send(mail);
	}

	@Override
	public void sendLaboratoryEmail(String toEmail, String citizenId, String password) {
		sendEmail(toEmail, "Laboratory Clinical Records Credentials", "Here are your credentials to access our laboratory.\n"
				+ "Citizen Card ID: " + citizenId + "\n"
				+ "Password: " + password);
		
	}

	@Override
	public void sendLaboratoryChangePasswordEmail(String toEmail) {
		sendEmail(toEmail, "Laboratory Clinical Records", "Your password has been changed.\n"
				+ "If you are not aware of this, please contact one of our services immediately.");
	}

}
