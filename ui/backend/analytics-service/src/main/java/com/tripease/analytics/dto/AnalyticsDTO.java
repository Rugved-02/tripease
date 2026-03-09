package com.tripease.analytics.dto;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data // Generates Getters, Setters, toString, equals, and hashCode
public class AnalyticsDTO {

    private String revenueTrendPercentage;
    private String customerTrendPercentage;
    private String flightTrendPercentage;
    private String hotelTrendPercentage;
    private double totalRevenue;
    private long totalCustomers;
    private long flightBookings;
    private long hotelBookings;
    private List<Double> revenueTrend;
    private Map<String, Double> bookingsDistribution;
    private List<Long> customerGrowth;
    private String peakBookingSeason;
    private double averageBookingValue;
    private String customerRetention;
}