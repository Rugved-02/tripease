package com.tripease.authservice.controller;

//import com.tripease.authservice.dto.ChangePasswordRequest;
import com.tripease.authservice.dto.RegisterRequest;
import com.tripease.authservice.model.User;
import com.tripease.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        System.out.println("AuthController:: /login");
        return ResponseEntity.ok(authService.login(request.get("email"), request.get("password")));
    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<Map<String, String>> verify(@RequestBody Map<String, String> request) {
//        String token = authService.verifyOtp(request.get("email"), request.get("code"), request.get("purpose"));
//        return ResponseEntity.ok(Map.of("token", token));
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
//        authService.initiateForgotPassword(request.get("email"));
//        return ResponseEntity.ok("OTP sent to your email.");
//    }
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> reset(
//            @AuthenticationPrincipal User user, // Token must be present
//            @RequestBody Map<String, String> request
//    ) {
//        return ResponseEntity.ok(authService.resetPassword(user.getEmail(), request.get("newPassword")));
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(
//            @AuthenticationPrincipal User user,
//            @RequestBody ChangePasswordRequest request
//    ) {
//        return ResponseEntity.ok(authService.changePassword(request, user));
//    }
}