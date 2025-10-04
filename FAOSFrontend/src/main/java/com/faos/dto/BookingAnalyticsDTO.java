package com.faos.dto;

import java.time.LocalDate;
import java.util.Map;

/**
 * Data Transfer Object for booking analytics and metrics.
 * Contains comprehensive booking statistics, revenue data, and trend analysis.
 * 
 * @author FAOS Development Team
 * @version 1.0
 */
public class BookingAnalyticsDTO {
    
    private long totalBookings;
    private long todayBookings;
    private long thisWeekBookings;
    private long thisMonthBookings;
    private long pendingBookings;
    private long completedBookings;
    private double totalRevenue;
    private double monthlyRevenue;
    private Map<String, Long> bookingsByDeliveryOption;
    private Map<String, Long> bookingsByPaymentOption;
    private Map<String, Long> bookingsByTimeSlot;
    private Map<LocalDate, Long> dailyBookingsLast30Days;
    private Map<String, Long> monthlyBookingsLast12Months;
    private Map<String, Double> revenueByMonth;
    private double averageBookingsPerDay;
    private String peakBookingTime;
    private String mostPopularDeliveryOption;
    private String mostPopularPaymentOption;

    // Constructors
    public BookingAnalyticsDTO() {}

    // Getters and Setters
    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTodayBookings() {
        return todayBookings;
    }

    public void setTodayBookings(long todayBookings) {
        this.todayBookings = todayBookings;
    }

    public long getThisWeekBookings() {
        return thisWeekBookings;
    }

    public void setThisWeekBookings(long thisWeekBookings) {
        this.thisWeekBookings = thisWeekBookings;
    }

    public long getThisMonthBookings() {
        return thisMonthBookings;
    }

    public void setThisMonthBookings(long thisMonthBookings) {
        this.thisMonthBookings = thisMonthBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Map<String, Long> getBookingsByDeliveryOption() {
        return bookingsByDeliveryOption;
    }

    public void setBookingsByDeliveryOption(Map<String, Long> bookingsByDeliveryOption) {
        this.bookingsByDeliveryOption = bookingsByDeliveryOption;
    }

    public Map<String, Long> getBookingsByPaymentOption() {
        return bookingsByPaymentOption;
    }

    public void setBookingsByPaymentOption(Map<String, Long> bookingsByPaymentOption) {
        this.bookingsByPaymentOption = bookingsByPaymentOption;
    }

    public Map<String, Long> getBookingsByTimeSlot() {
        return bookingsByTimeSlot;
    }

    public void setBookingsByTimeSlot(Map<String, Long> bookingsByTimeSlot) {
        this.bookingsByTimeSlot = bookingsByTimeSlot;
    }

    public Map<LocalDate, Long> getDailyBookingsLast30Days() {
        return dailyBookingsLast30Days;
    }

    public void setDailyBookingsLast30Days(Map<LocalDate, Long> dailyBookingsLast30Days) {
        this.dailyBookingsLast30Days = dailyBookingsLast30Days;
    }

    public Map<String, Long> getMonthlyBookingsLast12Months() {
        return monthlyBookingsLast12Months;
    }

    public void setMonthlyBookingsLast12Months(Map<String, Long> monthlyBookingsLast12Months) {
        this.monthlyBookingsLast12Months = monthlyBookingsLast12Months;
    }

    public Map<String, Double> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(Map<String, Double> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public double getAverageBookingsPerDay() {
        return averageBookingsPerDay;
    }

    public void setAverageBookingsPerDay(double averageBookingsPerDay) {
        this.averageBookingsPerDay = averageBookingsPerDay;
    }

    public String getPeakBookingTime() {
        return peakBookingTime;
    }

    public void setPeakBookingTime(String peakBookingTime) {
        this.peakBookingTime = peakBookingTime;
    }

    public String getMostPopularDeliveryOption() {
        return mostPopularDeliveryOption;
    }

    public void setMostPopularDeliveryOption(String mostPopularDeliveryOption) {
        this.mostPopularDeliveryOption = mostPopularDeliveryOption;
    }

    public String getMostPopularPaymentOption() {
        return mostPopularPaymentOption;
    }

    public void setMostPopularPaymentOption(String mostPopularPaymentOption) {
        this.mostPopularPaymentOption = mostPopularPaymentOption;
    }
}