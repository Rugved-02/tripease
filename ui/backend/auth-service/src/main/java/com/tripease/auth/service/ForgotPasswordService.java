package com.tripease.auth.service;

public interface ForgotPasswordService {
	public void sendOtp(String email);
	public boolean updatePassword(String email, String otp, String newPassword);
}
