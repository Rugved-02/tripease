package com.tripease.booking.repository;

import com.tripease.booking.model.Booking;
import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.model.ResourceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Fetch all bookings for a user, newest trips first
    List<Booking> findByUserIdOrderByStartDateDesc(String userId);

    // Fetch only active bookings of a specific type (e.g., all upcoming FLIGHTS)
    List<Booking> findByUserIdAndResourceType(String userId, String resourceType);

    // Finds bookings stuck in PENDING that were created before the 'threshold' time
    List<Booking> findAllByStatusAndCreatedAtBefore(BookingStatus status, LocalDateTime threshold);


    // Used by the API and other microservices to fetch booking details
    Optional<Booking> findByBookingReference(String bookingReference);


    long countByUserId(String userId);

    long countByUserIdAndStatusIn(String userId, List<BookingStatus> relevantStatuses);

    // Count bookings by status for a specific user
    long countByUserIdAndStatus(String userId, BookingStatus status);

    // Sum the total amount spent by a user (only for confirmed bookings usually)
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.userId = :userId AND b.status = :status")
    BigDecimal sumTotalAmountByUserIdAndStatus(@Param("userId") String userId, @Param("status") BookingStatus status);

    List<Booking> findTop5ByUserIdAndStatusInOrderByUpdatedAtDesc(
            String userId,
            List<BookingStatus> statuses
    );

    Slice<Booking> findByUserIdAndStatusIn(
            String userId,
            List<BookingStatus> statuses,
            Pageable pageable
    );


//    for booking analytics controller

    // For Total Revenue calculation
    @Query("SELECT b.totalAmount FROM Booking b WHERE b.status = 'CONFIRMED'")
    List<BigDecimal> findConfirmedPrices();

    // For Flight/Hotel count
    Long countByResourceType(ResourceType resourceType);

    // For Customer Retention: Counts users who have more than 1 booking
    @Query(value = "SELECT COUNT(*) FROM (SELECT user_id FROM bookings GROUP BY user_id HAVING COUNT(user_id) > 1) AS repeat_customers",
            nativeQuery = true)
    Long countReturningUsers();
}
