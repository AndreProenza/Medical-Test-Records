package com.laboratory.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CitizenCredentials {
	
	@NotNull(message = "Citizen ID cannot be null")
	@Pattern(regexp = ("^[0-9]{8}$"), message = "Citizen ID should have 8 numbers")
	private String citizenId;
	
	@NotNull(message = "Password cannot be null")
	@Pattern(regexp = ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"), 
	message = "Incorrect password")
	private String oldPassword;
	
	@NotNull(message = "Password cannot be null")
	@Pattern(regexp = ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"), 
	message = "New password should contain at least eight characters, one uppercase letter, "
			+ "one lowercase letter, one number and one special character")
	private String newPassword;
	
	@NotNull(message = "Password cannot be null")
	@Pattern(regexp = ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"), 
	message = "Confirmation password should contain at least eight characters, one uppercase letter, "
			+ "one lowercase letter, one number and one special character")
	private String confPassword;
	
	
	public CitizenCredentials() {
		//Does Nothing
	}
	
	
	public CitizenCredentials(String citizenId, String oldPassword, String newPassword, String confPassword) {
		super();
		this.citizenId = citizenId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confPassword = confPassword;
	}
	
	

	public String getCitizenId() {
		return citizenId;
	}


	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}


	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}


	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getConfPassword() {
		return confPassword;
	}


	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
	
	
}
