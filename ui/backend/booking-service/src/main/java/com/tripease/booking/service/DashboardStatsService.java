package com.tripease.booking.service;

import com.tripease.booking.client.FlightServiceClient;
import com.tripease.booking.client.HotelServiceClient;
import com.tripease.booking.dto.DashboardStatsResponseDTO;
import com.tripease.booking.dto.RecentBookingsResponseDTO;
import com.tripease.booking.dto.ResourceDetailsDTO;
import com.tripease.booking.dto.flight.FlightRecentBookingResponseDTO;
import com.tripease.booking.model.Booking;
import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.model.ResourceType;
import com.tripease.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardStatsService {

    private final BookingRepository bookingRepository;
    private final HotelServiceClient hotelServiceClient;
    private final FlightServiceClient flightServiceClient;

    public DashboardStatsResponseDTO getStatsByUserId(String userId) {

        List<BookingStatus> relevantStatuses = List.of(BookingStatus.CONFIRMED,BookingStatus.PENDING, BookingStatus.CANCELLED);

        // 1. Total is now strictly Confirmed + Cancelled
        long total = bookingRepository.countByUserIdAndStatusIn(userId, relevantStatuses);
        // Fetch counts
//        long total = bookingRepository.countByUserId(userId);
        long confirmed = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.CONFIRMED);
        long pending = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.PENDING);
        long expired = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.EXPIRED);
        long cancelled = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.CANCELLED);


        // Fetch sum (default to ZERO if no confirmed bookings exist)
        BigDecimal totalSpent = bookingRepository.sumTotalAmountByUserIdAndStatus(userId, BookingStatus.CONFIRMED);
        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }

        // Return the DTO (mapping numbers to Strings as per your Record definition)
        return DashboardStatsResponseDTO.builder()
                .totalBookings(String.valueOf(total))
                .confirmedBookings(String.valueOf(confirmed))
                .pendingBookings(String.valueOf(pending))
                .expiredBookings(String.valueOf(expired))
                .cancelledBookings(String.valueOf(cancelled))
                .totalSpent(totalSpent.setScale(2, RoundingMode.HALF_UP).toPlainString())
                .build();
    }


    public Slice<RecentBookingsResponseDTO> getRecentBookingsWithNames(String userId, int page, int size) {

        List<BookingStatus> allowedStatuses = List.of(
                BookingStatus.CONFIRMED,
                BookingStatus.PENDING,
                BookingStatus.EXPIRED
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Slice<Booking> bookingSlice = bookingRepository.findByUserIdAndStatusIn(userId, allowedStatuses, pageable);
//        // 1. Get the 5 bookings
//        List<Booking> bookings = bookingRepository.findTop5ByUserIdAndStatusInOrderByUpdatedAtDesc(
//                userId, allowedStatuses);

        // 2. Separate IDs by type to fetch names in bulk
        List<Long> hotelIds = bookingSlice.stream()
                .filter(b -> b.getResourceType() == ResourceType.HOTEL)
                .map(Booking::getResourceId)
                .collect(Collectors.toList());

        List<Long> flightIds = bookingSlice.stream()
                .filter(b -> b.getResourceType() == ResourceType.FLIGHT)
                .map(Booking::getResourceId)
                .collect(Collectors.toList());

        // 3. Fetch details into Maps (id -> details)
        Map<Long, ResourceDetailsDTO> hotelMap = hotelServiceClient.getHotelsByIds(hotelIds).getBody().stream()
                .collect(Collectors.toMap(h -> h.hotelId(), h -> ResourceDetailsDTO.builder()
                        .hotelName(h.hotelName())
                        .hotelLocation((h.location()))
                        .build()));

        Map<Long, ResourceDetailsDTO> flightMap = flightServiceClient.getFlightsByIds(flightIds).getBody().stream()
                .collect(Collectors.toMap(FlightRecentBookingResponseDTO::flightId, f -> ResourceDetailsDTO.builder()
                        .flightNo(f.flightNo())
                        .airline(f.airline())
                        .depPlace(f.depPlace())
                        .arrPlace(f.arrPlace())
                        .build()));


        List<RecentBookingsResponseDTO> result = bookingSlice.stream().map(b -> {
            ResourceDetailsDTO details = (b.getResourceType() == ResourceType.HOTEL)
                    ? hotelMap.get(b.getResourceId())
                    : flightMap.get(b.getResourceId());

            return RecentBookingsResponseDTO.builder()
                    .resourceType(b.getResourceType())
                    .subType(b.getSubType())
                    .bookingStatus(b.getStatus())
                    .startDate(b.getStartDate())
                    .endDate(b.getEndDate())
                    .totalAmount(b.getTotalAmount())
                    .createdAt(b.getCreatedAt())
                    .updatedAt(b.getUpdatedAt())
                    .resourceDetails(details) // Full info attached here
                    .build();
        }).toList();

        return new SliceImpl<>(result, pageable, bookingSlice.hasNext());
    }
}