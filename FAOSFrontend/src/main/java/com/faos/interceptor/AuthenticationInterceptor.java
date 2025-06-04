package com.faos.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    // List of URLs that don't require authentication
    private final List<String> publicUrls = Arrays.asList(
            "/login",
            "/css/main.css",
            "/css/components/header.css",
            "/css/components/forms.css",
            "/css/components/buttons.css",
            "/css/components/login.css",
            "/customer/updatepassword"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        // Allow access to public resources
        if (publicUrls.contains(requestURI) || requestURI.startsWith("/css/")) {
            return true;
        }

        // Check if user is authenticated
        String userType = (String) session.getAttribute("userType");

        // If not authenticated, redirect to login
        if (userType == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Handle home page access
        if (requestURI.equals("/")) {
            if (!"ADMIN".equals(userType)) {
                response.sendRedirect("/customer/dashboard");
                return false;
            }
            return true;
        }

        // Admin URL patterns
        if (requestURI.startsWith("/customers") ||
                requestURI.equals("/register") ||
                requestURI.startsWith("/update/") ||
                requestURI.startsWith("/deactivate/")) {
            if (!"ADMIN".equals(userType)) {
                response.sendRedirect("/login");
                return false;
            }
        }

        // Customer URL patterns
        if (requestURI.startsWith("/customer/")) {
            if (!"CUSTOMER".equals(userType)) {
                response.sendRedirect("/login");
                return false;
            }
            // Allow dashboard and updatepassword without ID check
            if (!requestURI.equals("/customer/dashboard") &&
                    !requestURI.equals("/customer/updatepassword")) {
                String userId = (String) session.getAttribute("userId");
                String urlCustomerId = extractCustomerId(requestURI);
                if (urlCustomerId != null && !urlCustomerId.equals(userId)) {
                    response.sendRedirect("/login");
                    return false;
                }
            }
        }

        return true;
    }

    private String extractCustomerId(String uri) {
        if (uri.startsWith("/customer/") && !uri.equals("/customer/dashboard")) {
            String[] parts = uri.split("/");
            if (parts.length > 2) {
                return parts[2];
            }
        }
        return null;
    }
}