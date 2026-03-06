package com.tripease.auth.dto;

public class ForgotPasswordDTO {
	// DTO for the first step (Email only)
	public record ForgotPasswordRequest(String email) {}

	// DTO for the final step (OTP + New Password)
	public record ResetPasswordRequest(String email, String otp, String newPassword) {}
}
