package com.tripease.analytics.service;

import com.tripease.analytics.client.BookingClient;
import com.tripease.analytics.client.UserClient;
import com.tripease.analytics.dto.AnalyticsDTO;
import com.tripease.analytics.dto.BookingDTO;
import com.tripease.analytics.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private UserClient userClient;

    public AnalyticsDTO getCompleteAnalytics() {
        // 1. Fetch Full Data for Charts and Trends
        List<BookingDTO> allBookings = bookingClient.getAllBookings(); // Assumed endpoint for charts
        List<UserDTO> allUsers = userClient.getAllUsers();             // Assumed endpoint for charts

        // 2. Fetch Aggregated Metrics from your specific Feign methods
        List<Double> prices = bookingClient.getConfirmedPrices();
        Long totalCustomers = userClient.getTotalUsers();
        Long flightCount = bookingClient.getFlightCount();
        Long hotelCount = bookingClient.getHotelCount();
        Long returningUsersCount = bookingClient.getReturningUsers();

        // Time boundaries for Trend logic
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfCurrentMonth = now.withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime startOfLastMonth = startOfCurrentMonth.minusMonths(1);

        AnalyticsDTO dto = new AnalyticsDTO();

        // 3. Core Metrics (Top Cards)
        double totalRev = prices.stream().mapToDouble(Double::doubleValue).sum();
        dto.setTotalRevenue(totalRev);
        dto.setTotalCustomers(totalCustomers != null ? totalCustomers : 0);
        dto.setFlightBookings(flightCount != null ? flightCount : 0);
        dto.setHotelBookings(hotelCount != null ? hotelCount : 0);

        // 4. Trend Calculations (Using the full lists)
        dto.setRevenueTrendPercentage(calculateTrend(
                allBookings.stream().filter(b -> b.getBookingDate().isAfter(startOfCurrentMonth)).mapToDouble(BookingDTO::getAmount).sum(),
                allBookings.stream().filter(b -> b.getBookingDate().isAfter(startOfLastMonth) && b.getBookingDate().isBefore(startOfCurrentMonth)).mapToDouble(BookingDTO::getAmount).sum()
        ));

        dto.setCustomerTrendPercentage(calculateTrend(
                allUsers.stream().filter(u -> u.getCreatedAt().isAfter(startOfCurrentMonth)).count(),
                allUsers.stream().filter(u -> u.getCreatedAt().isAfter(startOfLastMonth) && u.getCreatedAt().isBefore(startOfCurrentMonth)).count()
        ));

        // 5. Chart Data (Revenue Trend & Customer Growth)
        dto.setRevenueTrend(processRevenueTrend(allBookings));
        dto.setCustomerGrowth(processCustomerGrowth(allUsers));

        dto.setBookingsDistribution(Map.of(
                "Flights", (double) dto.getFlightBookings(),
                "Hotels", (double) dto.getHotelBookings(),
                "Itinerary", (double) allBookings.stream().filter(b -> "ITINERARY".equalsIgnoreCase(b.getType())).count()
        ));

        // 6. Insights (Bottom Cards)
        dto.setAverageBookingValue(prices.isEmpty() ? 0 : totalRev / prices.size());
        dto.setPeakBookingSeason(calculatePeakSeason(allBookings));
        dto.setCustomerRetention(calculateRetention(returningUsersCount, totalCustomers));

        return dto;
    }

    // Helper to calculate Retention based on your getReturningUsers() Feign method
    private String calculateRetention(Long returning, Long total) {
        if (total == null || total == 0 || returning == null) return "0%";
        return String.format("%.0f%%", ((double) returning / total) * 100);
    }

    private String calculateTrend(double current, double previous) {
        if (previous <= 0) return current > 0 ? "+100%" : "0%";
        double diff = ((current - previous) / previous) * 100;
        return String.format("%s%.1f%%", (diff >= 0 ? "+" : ""), diff);
    }

    private List<Double> processRevenueTrend(List<BookingDTO> bookings) {
        return bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getBookingDate().getMonth(), TreeMap::new, Collectors.summingDouble(BookingDTO::getAmount)))
                .values().stream().collect(Collectors.toList());
    }

    private List<Long> processCustomerGrowth(List<UserDTO> users) {
        return users.stream()
                .collect(Collectors.groupingBy(u -> u.getCreatedAt().getMonth(), TreeMap::new, Collectors.counting()))
                .values().stream().collect(Collectors.toList());
    }

    private String calculatePeakSeason(List<BookingDTO> bookings) {
        return bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getBookingDate().getMonth(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse("N/A");
    }
}



//
//package com.tripease.analytics.service;
//
//import com.tripease.analytics.dto.AnalyticsDTO;
//import com.tripease.analytics.dto.BookingDTO;
//import com.tripease.analytics.dto.UserDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class AnalyticsService {
//
//    @Autowired
//    private MockDataService mockDataService;
//
//    public AnalyticsDTO getCompleteAnalytics() {
//        // 1. Fetch Mock Data
//        List<BookingDTO> allBookings = mockDataService.getAllBookings();
//        List<UserDTO> allUsers = mockDataService.getAllUsers();
//
//        // Time boundaries for Trend logic
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime startOfCurrentMonth = now.withDayOfMonth(1).withHour(0).withMinute(0);
//        LocalDateTime startOfLastMonth = startOfCurrentMonth.minusMonths(1);
//
//        AnalyticsDTO dto = new AnalyticsDTO();
//
//        // 2. Core Metrics (Top Cards)
//        // Using direct mock counts to match your dashboard screenshots
//        double totalRev = mockDataService.getConfirmedPrices().stream().mapToDouble(Double::doubleValue).sum();
//        dto.setTotalRevenue(totalRev);
//        dto.setTotalCustomers(mockDataService.getTotalUsersCount());
//        dto.setFlightBookings(mockDataService.getFlightCount());
//        dto.setHotelBookings(mockDataService.getHotelCount());
//
//        // 3. Trend Calculations (Using the mock list distributions)
//        dto.setRevenueTrendPercentage(calculateTrend(
//                allBookings.stream().filter(b -> b.getBookingDate().isAfter(startOfCurrentMonth)).mapToDouble(BookingDTO::getAmount).sum(),
//                allBookings.stream().filter(b -> b.getBookingDate().isAfter(startOfLastMonth) && b.getBookingDate().isBefore(startOfCurrentMonth)).mapToDouble(BookingDTO::getAmount).sum()
//        ));
//
//        dto.setCustomerTrendPercentage(calculateTrend(
//                allUsers.stream().filter(u -> u.getCreatedAt().isAfter(startOfCurrentMonth)).count(),
//                allUsers.stream().filter(u -> u.getCreatedAt().isAfter(startOfLastMonth) && u.getCreatedAt().isBefore(startOfCurrentMonth)).count()
//        ));
//
//        // Mocking individual card trends to match UI screenshots
//        dto.setFlightTrendPercentage("-15.2%");
//        dto.setHotelTrendPercentage("+6.7%");
//
//        // 4. Chart Data (Revenue Trend & Customer Growth)
//        dto.setRevenueTrend(processRevenueTrend(allBookings));
//        dto.setCustomerGrowth(processCustomerGrowth(allUsers));
//
//        Map<String, Double> distribution = new HashMap<>();
//        distribution.put("Flights", (double) dto.getFlightBookings());
//        distribution.put("Hotels", (double) dto.getHotelBookings());
//        distribution.put("Itinerary", (double) allBookings.stream().filter(b -> "ITINERARY".equalsIgnoreCase(b.getType())).count());
//        dto.setBookingsDistribution(distribution);
//
//        // 5. Insights (Bottom Cards)
//        dto.setAverageBookingValue(542.0); // Directly matching screenshot requirements
//        dto.setPeakBookingSeason(calculatePeakSeason(allBookings));
//        dto.setCustomerRetention(calculateRetention(mockDataService.getReturningUsersCount(), dto.getTotalCustomers()));
//
//        return dto;
//    }
//
//    private String calculateRetention(Long returning, Long total) {
//        if (total == null || total == 0 || returning == null) return "0%";
//        return String.format("%.0f%%", ((double) returning / total) * 100);
//    }
//
//    private String calculateTrend(double current, double previous) {
//        if (previous <= 0) return current > 0 ? "+100%" : "0%";
//        double diff = ((current - previous) / previous) * 100;
//        return String.format("%s%.1f%%", (diff >= 0 ? "+" : ""), diff);
//    }
//
//    private List<Double> processRevenueTrend(List<BookingDTO> bookings) {
//        return bookings.stream()
//                .collect(Collectors.groupingBy(b -> b.getBookingDate().getMonth(), TreeMap::new, Collectors.summingDouble(BookingDTO::getAmount)))
//                .values().stream().collect(Collectors.toList());
//    }
//
//    private List<Long> processCustomerGrowth(List<UserDTO> users) {
//        return users.stream()
//                .collect(Collectors.groupingBy(u -> u.getCreatedAt().getMonth(), TreeMap::new, Collectors.counting()))
//                .values().stream().collect(Collectors.toList());
//    }
//
//    private String calculatePeakSeason(List<BookingDTO> bookings) {
//        return bookings.stream()
//                .collect(Collectors.groupingBy(b -> b.getBookingDate().getMonth(), Collectors.counting()))
//                .entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(entry -> entry.getKey().name())
//                .orElse("Summer"); // Default to match UI if list is small
//    }
//}