package com.tripease.hotels.client;

import com.tripease.hotels.dto.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "flight-service", url = "${flight.service.url}")
@FeignClient(name = "payment-service")
public interface PaymentServiceClient {

    @GetMapping("/payment/test/v1")
    public String test(String userId);


}

