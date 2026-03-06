package com.tripease.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(name = "hotel-service", url = "${hotel.service.url}")
@FeignClient(name = "hotel-service")
public interface HotelServiceClient {

    @PostMapping("/hotels/confirm/{bookingId}")
    void confirmBooking(@PathVariable("bookingId") String bookingId);
}
