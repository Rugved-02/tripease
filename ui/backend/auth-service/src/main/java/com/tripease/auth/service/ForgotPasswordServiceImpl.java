package com.tripease.auth.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripease.auth.exception.InvalidOtpException;
import com.tripease.auth.exception.OtpExpiredException;
import com.tripease.auth.exception.UserNotFoundException;
import com.tripease.auth.model.Otp;
import com.tripease.auth.model.User;
import com.tripease.auth.repository.OtpRepository;
import com.tripease.auth.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	


	    private final OtpRepository otpRepository;
	    private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final EmailService emailService;

	    // STEP 1: Process Forgot Password
	    @Override
	    public void sendOtp(String email) {
	    	
	    	log.info("Received OTP request for email: {}", email);
	        // 1. Check if user exists (Optional but recommended)
	        if (!userRepository.existsByEmail(email)) {
	        	log.warn("OTP request failed: Email {} not found in system", email);
	            throw new UserNotFoundException("No account found with the email: " + email);
	        }

	        // 2. Generate 6-digit OTP
	        String otp = String.format("%06d", new Random().nextInt(999999));
	        log.debug("Generated OTP for {}: [HIDDEN]", email);
	        
	        // 3. Save to DB (Delete any existing OTP for this email first)
	        otpRepository.deleteByEmail(email); 
	        Otp token = new Otp(email, otp, 5); // 5 mins expiry
	        otpRepository.save(token);
	        log.info("OTP saved to database for {}", email);

	        // 4. Send otp to Email
	        
	        try {
	            emailService.sendOtpToEmail(email, otp);
	            log.info("OTP email sent successfully to {}", email);
	        } 
	        
	        catch (MessagingException e) {
	            log.error("Failed to send OTP email to {}: {}", email, e.getMessage());
	            throw new RuntimeException("Email service is down. Please try again later.");
	       }
	    }

	    // STEP 2: Verify and Update Password
	    @Override
	    @Transactional
	    public boolean updatePassword(String email, String otp, String newPassword) {
	    	
	    	log.info("Attempting password update for email: {}", email);
	    	
	        Optional<Otp> otpTokenOpt = otpRepository.findByEmail(email);

	        if (otpTokenOpt.isEmpty()) {
	        	log.error("Password update failed: No OTP record found for {}", email);
	            throw new RuntimeException("Invalid request. Please request a new OTP.");
	        }

	        Otp storedToken = otpTokenOpt.get();

	        if (storedToken.isExpired()) {
	        	log.warn("Password update failed: OTP expired for {}", email);
	            otpRepository.delete(storedToken);
	            throw new OtpExpiredException("OTP has expired. Please request a new one.");
	        }

	        if (!storedToken.getOtp().equals(otp)) {
	        	log.warn("Password update failed: Incorrect OTP entered for {}", email);
	        	throw new InvalidOtpException("The OTP entered is incorrect.");	      
	        }

	        // If we reach here, validation passed
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() ->{
	                	log.error("Critical Error: User {} disappeared during reset process", email);
	                	return new UserNotFoundException("User no longer exists.");
	                	});
	        
	        user.setPassword(passwordEncoder.encode(newPassword));
	        userRepository.save(user);
	        otpRepository.delete(storedToken);
	        log.info("Password successfully updated and OTP cleared for user: {}", email);
	        return true;
	    }
	
}
