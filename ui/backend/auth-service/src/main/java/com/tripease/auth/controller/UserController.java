package com.tripease.auth.controller;

import com.tripease.auth.dto.*;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.tripease.auth.dto.ForgotPasswordDTO.ForgotPasswordRequest;
import com.tripease.auth.dto.ForgotPasswordDTO.ResetPasswordRequest;
import com.tripease.auth.model.User;
import com.tripease.auth.security.JwtUtil;
import com.tripease.auth.service.ForgotPasswordService;
import com.tripease.auth.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final JwtUtil jwtUtil;
	private final UserService service;
	private final ForgotPasswordService mailService;


    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody Map<String, String> payload) {
        // 1. Extract the encoded string from the wrapper
        String encodedData = payload.get("data");

        if (encodedData == null || encodedData.isEmpty()) {
            log.error("Registration failed: Missing 'data' field in request body");
            return ResponseEntity.badRequest().build();
        }

        // 2. Call the service method that handles the decoding AND the saving
        // This allows your service to manage the @Transactional boundary
        User savedUser = service.saveUserFromEncoded(encodedData);

        // 3. Map back to Response DTO
        UserResponseDTO response = mapToUserResponse(savedUser);

        log.info("User registered successfully: {}", savedUser.getEmail());
        return ResponseEntity.ok(response);
    }
	
	
	@PostMapping("login")
	ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){

		log.info("Login attempt for user: {}", loginRequestDTO.getEmail());

		//It triggers globalException if any exception comes in below line
		service.checkCredentials(decodeBase64(loginRequestDTO.getEmail()), decodeBase64(loginRequestDTO.getPassword()));

        //        Getting userId to set in JWT claims
        String userId = service.getUserIdByEmail(decodeBase64(loginRequestDTO.getEmail()));

	    // If we reached this line, the login was successful
	    String token = jwtUtil.generateToken(decodeBase64(loginRequestDTO.getEmail()), userId);
	    log.info("Login successful for user: {}. Token generated.", loginRequestDTO.getEmail());
	    return ResponseEntity.ok(new AuthResponseDTO(token));
	}
	
//	@GetMapping("users/{userId}")
	@GetMapping("users/me")
	ResponseEntity<UserResponseDTO> getUser(@AuthenticationPrincipal String email){
		log.info("Fetching profile details for authenticated user: {}", email);
		User user=service.getUser(email);
		return ResponseEntity.ok(mapToUserResponse(user));
	}

    @GetMapping("user/profile")
    ResponseEntity<UserResponseDTO> getUserProfile(@RequestHeader("X-User-Id") String userId){
        log.info("Fetching profile details for authenticated user: {}", userId);
        User user=service.getUserProfile(userId);
        return ResponseEntity.ok(mapToUserResponse(user));
    }
	@PostMapping("users/me")
	ResponseEntity<UserResponseDTO> updateUser(@RequestBody UpdateUserDTO user, @AuthenticationPrincipal String email){
		log.info("Received update request for user profile: {}", email);
		
		User updatingUser= service.updateUser(email,user);
		log.info("Profile updated successfully for user: {}", email);
		
		return ResponseEntity.ok(mapToUserResponse(updatingUser));
	}
	
	
	
	
	@PostMapping("forgotPassword")
    public ResponseEntity<String> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
		
		log.info("Password reset requested for email: {}", request.email());
		
        mailService.sendOtp(request.email());
        log.info("OTP sent/processed for email: {}", request.email());
        
        return ResponseEntity.ok("If an account exists for " + request.email() + ", an OTP has been sent.");
    }
	
	
	
	
	@PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

		log.info("Attempting password reset for email: {}", request.email());
        boolean success = mailService.updatePassword(request.email(), request.otp(), request.newPassword());
        
        if (success) {
        	log.info("Password reset successful for email: {}", request.email());
            return ResponseEntity.ok("Password updated successfully.");
        } else {
        	log.warn("Password reset failed for email: {} (Invalid/Expired OTP)", request.email());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
        }
    }
	
	private UserResponseDTO mapToUserResponse(User user) {
	    return UserResponseDTO.builder()
	            .userId(user.getUserId())
	            .name(user.getName())
	            .email(user.getEmail())
	            .mobile(user.getMobile())
	            .createdAt(user.getCreatedAt())
	            .build();
	}

    private String decodeBase64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }
		
}
