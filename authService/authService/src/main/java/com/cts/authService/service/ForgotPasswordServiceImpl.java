package com.cts.authService.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.authService.exception.InvalidOtpException;
import com.cts.authService.exception.OtpExpiredException;
import com.cts.authService.exception.UserNotFoundException;
import com.cts.authService.model.Otp;
import com.cts.authService.model.User;
import com.cts.authService.repository.OtpRepository;
import com.cts.authService.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	


	    private OtpRepository otpRepository;
	    private UserRepository userRepository;
	    private PasswordEncoder passwordEncoder;
	    private JavaMailSender mailSender;

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
	            sendEmail(email, otp);
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

	    private void sendEmail(String to, String otp) throws MessagingException{
	    	
	    	log.info("Attempting to send OTP email to: {}", to);
	    	
	        MimeMessage message = mailSender.createMimeMessage();
	        
	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(to);
	            helper.setSubject("Password Reset OTP");
	            
	            // Using true here tells Spring this is HTML
	            String htmlContent = "<h3>Your OTP is: <b style='color:blue;'>" + otp + "</b></h3>" +
	                                 "<p>Valid for 5 minutes.</p>";
	                                 
	            helper.setText(htmlContent, true); 
	            mailSender.send(message);
	            log.info("Successfully sent OTP email to: {}", to);
	            
	        } 
	        
	        catch (MessagingException e) {	
	            // Log of full stack trace here to recognize if it's an Auth error, Network error, etc.
	        	log.error("SMTP failure for user {}: {}", to, e.getMessage());
	            throw e;
	        } 
	        
	        catch (Exception e) {
	            log.error("An unexpected error occurred while preparing email for {}: ", to, e);
	            throw e;
	        }
	    }
		

	
}
