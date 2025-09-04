package com.faos.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.faos.dto.CustomerBookingReport;
import com.faos.exception.InvalidDateRangeException;
import com.faos.model.Customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController {

    private static final String BACKENED_URL = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/report")
    public String report(HttpSession session) {
        String userType = (String) session.getAttribute("userType");

        if (userType == null || "CUSTOMER".equals(userType)) {
            return "redirect:/";
        }
    
        return "report";
    }
    

    @PostMapping("/report/max-bookings")
public String getCustomerReportWithMaxBookings(
        @RequestParam(value = "startDate", required = false) String startDateString,
        @RequestParam(value = "endDate", required = false) String endDateString,
        Model model) {

    try {
        // Check if dates are provided
        if (startDateString == null || startDateString.isEmpty() || endDateString == null || endDateString.isEmpty()) {
            throw new IllegalArgumentException("Start date and end date must not be empty.");
        }

        LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date.");
        }

        ResponseEntity<List<CustomerBookingReport>> response = restTemplate.exchange(
                BACKENED_URL + "/report/max-bookings?startDate=" + startDate + "&endDate=" + endDate,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerBookingReport>>() {});

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("customers", response.getBody());
            return "report";  // Returning the correct view instead of redirecting
        } else {
            throw new RuntimeException("Failed to fetch data from backend.");
        }
    } catch (InvalidDateRangeException | IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "report";
    } catch (Exception e) {
        model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        return "report";
    }
}
 @GetMapping("/report/inactive-customers")
    @ResponseBody
    public ResponseEntity<?> getInactiveCustomers() {
        try {
            ResponseEntity<List<Customer>> response = restTemplate.exchange(
                    "http://localhost:8080" + "/report/inactive-customers",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Customer>>() {
                    });

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

}


