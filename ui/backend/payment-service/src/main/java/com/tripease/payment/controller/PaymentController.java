package com.tripease.payment.controller;

import com.tripease.payment.dto.PaymentRequestDTO;
import com.tripease.payment.dto.PaymentResponseDTO;
import com.tripease.payment.model.Payment;
import com.tripease.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
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
    @GetMapping("/booking/{bookingId}")
    String getPaymentStatus(@PathVariable String bookingId){
        return paymentService.getLatestPaymentStatusForBookingId(bookingId);
    }

}

