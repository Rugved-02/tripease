package com.cts.authService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.authService.dto.AuthResponse;
import com.cts.authService.dto.ForgotPassword.ForgotPasswordRequest;
import com.cts.authService.dto.ForgotPassword.ResetPasswordRequest;
import com.cts.authService.dto.LoginRequest;
import com.cts.authService.dto.UpdateUser;
import com.cts.authService.model.User;
import com.cts.authService.security.JwtUtil;
import com.cts.authService.service.ForgotPasswordService;
import com.cts.authService.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class UserController {
	JwtUtil jwtUtil;
	UserService service;
	ForgotPasswordService mailService;
	
	@PostMapping("register")
	ResponseEntity<User> saveUser(@RequestBody User user){
		System.out.println("UserController::saveUser()");
		service.saveUser(user);
		ResponseEntity re= ResponseEntity.ok(user);
		return re;
	}
	
	
	@PostMapping("login")
	ResponseEntity<?> login(@RequestBody LoginRequest login){
		System.out.println("UserController::login()");
		//It triggers globalException if any exception comes in below line (42)
		service.checkCredentials(login.getEmail(), login.getPassword());
	    
	    // If we reached this line, the login was successful
	    String token = jwtUtil.generateToken(login.getEmail());
	    return ResponseEntity.ok(new AuthResponse(token));
	}
	
//	@GetMapping("users/{userId}")
	@GetMapping("users/me")
	ResponseEntity<User> getUser(@AuthenticationPrincipal String email){
		System.out.println("UserController::getUser()");
		User user=service.getUser(email);
		ResponseEntity re = ResponseEntity.ok(user);
		return re;
	}
	
	@PostMapping("users/me")
	ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser user,@AuthenticationPrincipal String email){
		System.out.println("UserController::updateUser()");
		UpdateUser updatingUser= service.updateUser(email,user);
		return ResponseEntity.ok(updatingUser);
	}
	
	
	
	
	@PostMapping("forgotPassword")
    public ResponseEntity<String> processForgotPassword(@RequestBody ForgotPasswordRequest request) {
		System.out.println("Forgotpassword=======================");
        mailService.sendOtp(request.email());
        return ResponseEntity.ok("If an account exists for " + request.email() + ", an OTP has been sent.");
    }
	
	
	
	
	@PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

        boolean success = mailService.updatePassword(request.email(), request.otp(), request.newPassword());
        
        if (success) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
        }
    }
	
	
	
	
	
	
	
	
	
	
}
