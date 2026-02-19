package com.tripease.authservice.service;

import com.tripease.authservice.dto.RegisterRequest;
import com.tripease.authservice.dto.UserResponse;
import com.tripease.authservice.model.*;
import com.tripease.authservice.repository.*;
import com.tripease.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    //    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    //    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .isActive(true)
                .build();

        System.out.println("AuthService:: Register");

        User savedUser = userRepository.save(user);
//        savedUser.setCreatedBy(savedUser);

//        sendFormattedOtp(savedUser,"REGISTRATION");
//        return "Admin registered. Verify using the TL-prefixed OTP sent to email.";
        return "User Registered";
    }

//    public UserResponse createOperator(CreateOperatorRequest request, String adminEmail) {
//        User admin = userRepository.findByEmail(adminEmail)
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        User operator = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .companyName(admin.getCompanyName())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole())
//                .isActive(true)
//                .mustResetPassword(true)
//                .createdBy(admin)
//                .build();
//
//        User savedUser = userRepository.save(operator);
//
//        return UserResponse.builder()
//                .id(savedUser.getId())
//                .firstName(savedUser.getFirstName())
//                .lastName(savedUser.getLastName())
//                .email(savedUser.getEmail())
//                .role(savedUser.getRole())
//                .isActive(savedUser.isActive())
//                .createdAt(savedUser.getCreatedAt())
//                .build();
//    }

    public Map<String, Object> login(String email, String password) {

        try {
            System.out.println("AuthService:: Login - Attempting auth for: " + email);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            // 1. Check if the user actually exists and catch the specific error
            UserDetails user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found in DB: " + email));

            // 2. Avoid .toString() if you suspect circular dependencies
            System.out.println("User found: " + user.getUsername());

            return Map.of("status", "SUCCESS", "token", jwtService.generateToken(user));

        } catch (Exception e) {
            System.err.println("ERROR in login process: " + e.getMessage());
            e.printStackTrace(); // This will tell you exactly what line failed and why
            throw e;
        }}

//    @Transactional
//    public String verifyOtp(String email, String code, String purpose) {
//        var user = userRepository.findByEmail(email).orElseThrow();
//        var otp = otpRepository.findByCodeAndUserAndPurpose(code, user, purpose)
//                .orElseThrow(() -> new RuntimeException("Invalid OTP"));
//
//        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Expired");
//
//        if ("REGISTRATION".equals(purpose)) user.setActive(true);
//
//        otpRepository.delete(otp);
//        return jwtService.generateToken(user);
//    }
//
//
//
//    private void sendFormattedOtp(User user, String purpose) {
//        String code = "TL" + (1000 + new Random().nextInt(9000));
//
//        // Using Builder to resolve constructor issues
//        OtpVerification otpEntry = OtpVerification.builder()
//                .code(code)
//                .purpose(purpose)
//                .user(user)
//                .expiresAt(LocalDateTime.now().plusMinutes(10))
//                .build();
//
//        otpRepository.save(otpEntry);
//        emailService.sendOtpEmail(user.getEmail(), code);
//    }
//
//    public void initiateForgotPassword(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        sendFormattedOtp(user, "FORGOT_PASSWORD");
//    }

    // 2. RESET PASSWORD (Used after OTP verification provides a token)
//    @Transactional
//    public String resetPassword(String authenticatedEmail, String newPassword) {
//        User user = userRepository.findByEmail(authenticatedEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setPassword(passwordEncoder.encode(newPassword));
//        user.setMustResetPassword(false); // Clear flag in case it was a new user
//        userRepository.save(user);
//        return "Password reset successful. Use your new password to login.";
//    }

    // 3. CHANGE PASSWORD (Used for First Login / Security Settings)
//    @Transactional
//    public String changePassword(ChangePasswordRequest request, User currentUser) {
//        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
//            throw new RuntimeException("Current password does not match");
//        }
//
//        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        currentUser.setMustResetPassword(false); // This unlocks the user account
//        userRepository.save(currentUser);
//        return "Password changed successfully.";
//    }
}
