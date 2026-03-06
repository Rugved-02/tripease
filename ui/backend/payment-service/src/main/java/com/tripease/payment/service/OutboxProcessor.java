package com.tripease.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripease.payment.client.BookingServiceClient;
import com.tripease.payment.config.GlobalSecurityStore;
import com.tripease.payment.dto.BookingRequestDTO;
import com.tripease.payment.model.BookingStatus;
import com.tripease.payment.model.OutboxEvent;
import com.tripease.payment.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final BookingServiceClient bookingServiceClient; // Your Feign Client
    private final ObjectMapper objectMapper; // Spring Boot provides this automatically

    @Scheduled(fixedDelay = 10000) // Runs every 10 seconds
    @Transactional
    public void processOutbox() {
        // Find events that haven't been sent yet in order of which was first and latest will be at the last
        List<OutboxEvent> events = outboxRepository.findByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent event : events) {
            try {
                log.info("Outbox: Notifying Booking Service for Booking {}", event.getAggregateId());
                // Call Flight Service via Feign

                String authData[] = GlobalSecurityStore.store.get("authData");

                log.info("Extracted Data from GlobalSecurityStore :: {},{},{}", authData[0], authData[1], authData[2]);

                if(event.getEventType().equals("PAYMENT_CONFIRMED")){
                    log.info("Status of outbox in outbox processor"+event.getEventType());
                    bookingServiceClient.confirmBooking(event.getAggregateId());
                }else{
                    bookingServiceClient.cancelBooking(event.getAggregateId());
                }


                // If successful, mark as PROCESSED
                event.setStatus("PROCESSED");
                outboxRepository.save(event);

            } catch (Exception e) {
                log.error("Outbox: Failed to notify Booking Service. Will retry later.");
                log.error(e.getMessage());
                event.setRetryCount(event.getRetryCount() + 1);

                if (event.getRetryCount() > 5) {
                    event.setStatus("FAILED"); // Stop retrying after 5 attempts
                }
                outboxRepository.save(event);
            }
        }
    }
}