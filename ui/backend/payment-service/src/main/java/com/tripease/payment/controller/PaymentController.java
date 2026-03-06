package com.tripease.payment.controller;

import com.tripease.payment.dto.PaymentRequestDTO;
import com.tripease.payment.dto.PaymentResponseDTO;
import com.tripease.payment.model.Payment;
import com.tripease.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    String demoPayment(){
        return "Hi from Payment";
    }

    @PostMapping("/process")
    PaymentResponseDTO processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO){

        PaymentResponseDTO paymentResponseDTO = paymentService.processPayment(paymentRequestDTO);
        return paymentResponseDTO;

    }
    @PostMapping("/payNow")
    public PaymentResponseDTO handlePayNow(
            @RequestHeader("Idempotency-Key")
            @NotBlank(message = "Idempotency-Key is missing or blank")
            @Pattern(
                    regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                    message = "Idempotency-Key must be a valid UUID format"
            )
            String idempotencyKey
    ) {
        return paymentService.payNow(idempotencyKey);
    }

    @GetMapping("/booking/{bookingId}")
    String getPaymentStatus(@PathVariable String bookingId){
        return paymentService.getLatestPaymentStatusForBookingId(bookingId);
    }

    @GetMapping("/test")
    public String testSecurity() {
        log.info("PaymentController /test endpoint invoked");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + (auth != null ? auth.getPrincipal() : "null"));
        log.info("Is Authenticated: " + (auth != null ? auth.isAuthenticated() : "null"));
        log.info("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
        return "Success";
    }

    @GetMapping("/test/v1")
    public String test(@AuthenticationPrincipal String userId) {
        log.info("UserId is : {}", userId);
        return "User is: " + userId; // If this returns the ID, it's working!
    }

}
