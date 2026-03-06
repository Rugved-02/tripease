package com.tripease.auth.service;


import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
    public void sendOtpToEmail(String to, String otp) throws MessagingException{
    	
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
