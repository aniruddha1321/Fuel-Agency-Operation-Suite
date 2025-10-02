package com.faos.service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.dto.BookingAnalyticsDTO;
import com.faos.repository.BookingRepository;

/**
 * Service class for booking analytics and reporting functionality.
 * Provides methods to calculate various booking metrics, trends, and insights.
 * 
 * @author FAOS Development Team
 * @version 1.0
 */
@Service
public class BookingAnalyticsService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Retrieves comprehensive booking analytics data including metrics, trends, and insights.
     * 
     * @return BookingAnalyticsDTO containing all analytics data
     */
    public BookingAnalyticsDTO getBookingAnalytics() {
        BookingAnalyticsDTO analytics = new BookingAnalyticsDTO();
        
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate thirtyDaysAgo = today.minusDays(30);
        LocalDate twelveMonthsAgo = today.minusMonths(12);

        // Basic counts
        analytics.setTotalBookings(bookingRepository.count());
        analytics.setTodayBookings(bookingRepository.countBookingsByDate(today));
        analytics.setThisWeekBookings(bookingRepository.countBookingsBetweenDates(startOfWeek, today));
        analytics.setThisMonthBookings(bookingRepository.countBookingsBetweenDates(startOfMonth, today));

        // For pending/completed bookings, we'll use delivery date as a proxy
        // Pending: delivery date is in the future
        // Completed: delivery date is in the past
        analytics.setPendingBookings(bookingRepository.countBookingsBetweenDates(today.plusDays(1), today.plusYears(1)));
        analytics.setCompletedBookings(bookingRepository.countBookingsBetweenDates(startOfYear, today.minusDays(1)));

        // Revenue calculations
        Double totalRevenue = bookingRepository.getTotalRevenueBetweenDates(startOfYear, today);
        analytics.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        
        Double monthlyRevenue = bookingRepository.getTotalRevenueBetweenDates(startOfMonth, today);
        analytics.setMonthlyRevenue(monthlyRevenue != null ? monthlyRevenue : 0.0);

        // Distribution by delivery option
        Map<String, Long> deliveryOptions = bookingRepository.countBookingsByDeliveryOption()
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0], 
                    row -> (Long) row[1],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setBookingsByDeliveryOption(deliveryOptions);

        // Distribution by payment option
        Map<String, Long> paymentOptions = bookingRepository.countBookingsByPaymentOption()
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0], 
                    row -> (Long) row[1],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setBookingsByPaymentOption(paymentOptions);

        // Distribution by time slot
        Map<String, Long> timeSlots = bookingRepository.countBookingsByTimeSlot()
                .stream()
                .collect(Collectors.toMap(
                    row -> (String) row[0], 
                    row -> (Long) row[1],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setBookingsByTimeSlot(timeSlots);

        // Daily booking statistics for the last 30 days
        Map<LocalDate, Long> dailyBookings = bookingRepository.getDailyBookingCounts(thirtyDaysAgo)
                .stream()
                .collect(Collectors.toMap(
                    row -> (LocalDate) row[0], 
                    row -> (Long) row[1],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setDailyBookingsLast30Days(dailyBookings);

        // Monthly bookings for last 12 months
        Map<String, Long> monthlyBookings = bookingRepository.getMonthlyBookingCounts(twelveMonthsAgo)
                .stream()
                .collect(Collectors.toMap(
                    row -> String.format("%d-%02d", (Integer) row[0], (Integer) row[1]), 
                    row -> (Long) row[2],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setMonthlyBookingsLast12Months(monthlyBookings);

        // Monthly revenue for last 12 months
        Map<String, Double> monthlyRevenueMap = bookingRepository.getMonthlyRevenue(twelveMonthsAgo)
                .stream()
                .collect(Collectors.toMap(
                    row -> String.format("%d-%02d", (Integer) row[0], (Integer) row[1]), 
                    row -> (Double) row[2],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setRevenueByMonth(monthlyRevenueMap);

        // Calculate derived metrics
        double averageBookingsPerDay = analytics.getTotalBookings() / 365.0;
        analytics.setAverageBookingsPerDay(Math.round(averageBookingsPerDay * 100.0) / 100.0);

        // Most popular options
        analytics.setPeakBookingTime(timeSlots.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A"));

        analytics.setMostPopularDeliveryOption(deliveryOptions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A"));

        analytics.setMostPopularPaymentOption(paymentOptions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A"));

        return analytics;
    }

    /**
     * Retrieves booking analytics data for a specific date range.
     * 
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return BookingAnalyticsDTO containing analytics data for the specified range
     */
    public BookingAnalyticsDTO getBookingAnalyticsForDateRange(LocalDate startDate, LocalDate endDate) {
        BookingAnalyticsDTO analytics = new BookingAnalyticsDTO();
        
        // Basic counts for the date range
        analytics.setTotalBookings(bookingRepository.countBookingsBetweenDates(startDate, endDate));
        
        // Revenue for the date range
        Double totalRevenue = bookingRepository.getTotalRevenueBetweenDates(startDate, endDate);
        analytics.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);

        // Daily booking statistics within the specified date range
        Map<LocalDate, Long> dailyBookings = bookingRepository.getDailyBookingCounts(startDate)
                .stream()
                .filter(row -> {
                    LocalDate date = (LocalDate) row[0];
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .collect(Collectors.toMap(
                    row -> (LocalDate) row[0], 
                    row -> (Long) row[1],
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
        analytics.setDailyBookingsLast30Days(dailyBookings);

        return analytics;
    }
}