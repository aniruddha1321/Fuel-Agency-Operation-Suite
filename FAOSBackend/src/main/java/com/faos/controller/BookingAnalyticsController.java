package com.faos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faos.dto.BookingAnalyticsDTO;
import com.faos.service.BookingAnalyticsService;

/**
 * REST Controller for booking analytics endpoints.
 * Provides API endpoints for retrieving booking statistics and analytics data.
 * 
 * @author FAOS Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/analytics")
public class BookingAnalyticsController {

    @Autowired
    private BookingAnalyticsService bookingAnalyticsService;

    @GetMapping("/bookings")
    public ResponseEntity<BookingAnalyticsDTO> getBookingAnalytics() {
        try {
            BookingAnalyticsDTO analytics = bookingAnalyticsService.getBookingAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bookings/daterange")
    public ResponseEntity<BookingAnalyticsDTO> getBookingAnalyticsForDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().build();
            }
            
            BookingAnalyticsDTO analytics = bookingAnalyticsService.getBookingAnalyticsForDateRange(startDate, endDate);
            return ResponseEntity.ok(analytics);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}