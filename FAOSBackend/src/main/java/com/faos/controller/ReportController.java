package com.faos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faos.dto.CustomerBookingReport;
import com.faos.exception.InvalidDateRangeException;
import com.faos.model.Customer;
import com.faos.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/max-bookings")
    public ResponseEntity<?> getCustomersWithMaxBookings(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            // Validate date range
            if (startDate.isAfter(endDate)) {
                throw new InvalidDateRangeException("Start date cannot be after end date.");
            }

            // Fetch report data
            List<CustomerBookingReport> report = reportService.getCustomersWithMaxBookings(startDate, endDate);
            return ResponseEntity.ok(report);

        } catch (InvalidDateRangeException e) {
            // Handle invalid date range exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @GetMapping("/inactive-customers")
    public ResponseEntity<?> getInactiveCustomers() {
        try {
            List<Customer> report = reportService.getCustomersWithNoRecentBookings();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occured " + e.getMessage());

        }
    }
}
