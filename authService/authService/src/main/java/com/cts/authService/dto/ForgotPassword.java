package com.cts.authService.dto;

public class ForgotPassword {
	// DTO for the first step (Email only)
	public record ForgotPasswordRequest(String email) {}

	// DTO for the final step (OTP + New Password)
	public record ResetPasswordRequest(String email, String otp, String newPassword) {}
}
