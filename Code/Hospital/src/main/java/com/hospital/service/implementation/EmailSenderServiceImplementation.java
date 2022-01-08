package com.hospital.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hospital.service.EmailSenderService;

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
	public void sendHospitalEmail(String toEmail, String citizenId, String password) {
		sendEmail(toEmail, "Laboratory Credentials", "Here are your credentials to access our Hospital.\n"
				+ "Citizen Card ID: " + citizenId + "\n"
				+ "Password: " + password);
		
	}

}
