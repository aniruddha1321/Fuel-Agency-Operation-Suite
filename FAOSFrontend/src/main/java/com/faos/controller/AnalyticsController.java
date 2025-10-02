package com.faos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.faos.dto.BookingAnalyticsDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Web Controller for the analytics dashboard.
 * Handles web requests for the booking analytics dashboard interface.
 * 
 * @author FAOS Development Team
 * @version 1.0
 */
@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    @Value("${backend.url:http://localhost:8080}")
    private String backendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/dashboard")
    public String getAnalyticsDashboard(Model model, HttpServletRequest request) {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userType"))) {
            return "redirect:/login";
        }

        try {
            // Fetch analytics data from backend
            ResponseEntity<BookingAnalyticsDTO> response = restTemplate.getForEntity(
                backendUrl + "/api/analytics/bookings", 
                BookingAnalyticsDTO.class
            );

            BookingAnalyticsDTO analytics = response.getBody();
            if (response.getStatusCode().is2xxSuccessful() && analytics != null) {
                model.addAttribute("analytics", analytics);
                
                // Add additional calculated metrics
                model.addAttribute("totalBookingsFormatted", String.format("%,d", analytics.getTotalBookings()));
                model.addAttribute("totalRevenueFormatted", String.format("₹%.2f", analytics.getTotalRevenue()));
                model.addAttribute("monthlyRevenueFormatted", String.format("₹%.2f", analytics.getMonthlyRevenue()));
                
                // Calculate growth percentages
                model.addAttribute("bookingGrowth", calculateBookingGrowth(analytics));
                model.addAttribute("revenueGrowth", calculateRevenueGrowth(analytics));
            } else {
                model.addAttribute("error", "Unable to fetch analytics data");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error connecting to analytics service: " + e.getMessage());
        }

        return "analytics-dashboard";
    }

    private double calculateBookingGrowth(BookingAnalyticsDTO analytics) {
        // This is a simplified calculation
        // In a real scenario, you would compare with previous month/week data
        if (analytics.getThisMonthBookings() == 0) return 0.0;
        
        double weeklyAverage = (double) analytics.getThisWeekBookings() / 7;
        double monthlyAverage = (double) analytics.getThisMonthBookings() / 30;
        
        if (monthlyAverage == 0) return 0.0;
        
        return ((weeklyAverage - monthlyAverage) / monthlyAverage) * 100;
    }

    private double calculateRevenueGrowth(BookingAnalyticsDTO analytics) {
        // This is a simplified calculation
        // In a real scenario, you would compare with previous month data
        if (analytics.getTotalRevenue() == 0) return 0.0;
        
        double currentMonthAverage = analytics.getMonthlyRevenue();
        double totalAverage = analytics.getTotalRevenue() / 12; // Assuming 12 months of data
        
        if (totalAverage == 0) return 0.0;
        
        return ((currentMonthAverage - totalAverage) / totalAverage) * 100;
    }
}