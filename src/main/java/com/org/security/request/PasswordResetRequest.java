package com.org.security.request;

public class PasswordResetRequest {
	
	private String oldPassword;
	
	private String newPassword;

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

	public PasswordResetRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordResetRequest(String oldPassword, String newPassword) {
		super();
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	

}
