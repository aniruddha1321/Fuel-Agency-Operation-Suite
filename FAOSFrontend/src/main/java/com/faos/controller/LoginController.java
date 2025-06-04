package com.faos.controller;

import com.faos.model.Login;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private static final String BACKEND_URL = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String userId,
                               @RequestParam String password,
                               @RequestParam String userType,
                               Model model,
                               HttpSession session) {
        try {
            Login loginRequest = new Login();
            loginRequest.setUserId(userId);
            loginRequest.setPassword(password);
            loginRequest.setUserType(userType);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    BACKEND_URL + "/login",
                    loginRequest,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                session.setAttribute("userId", userId);
                session.setAttribute("userType", userType);

                // Redirect based on user type
                if ("ADMIN".equals(userType)) {
                    return "redirect:/";
                } else if ("CUSTOMER".equals(userType)) {
                    return "redirect:/customer/dashboard";
                }
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = "Invalid credentials. Please try again.";
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                errorMessage = "Invalid username or password";
            }
            model.addAttribute("error", errorMessage);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again later.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    protected boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("userId") != null &&
                session.getAttribute("userType") != null;
    }

    @PostMapping("customer/updatepassword")
    public String updatePass(@RequestParam String consumerId, @RequestParam String consumerPass, RedirectAttributes redirectAttributes) {
        Login loginRequest = new Login();
        loginRequest.setUserId(consumerId);
        loginRequest.setPassword(consumerPass);

        try {
            HttpEntity<Login> requestEntity = new HttpEntity<>(loginRequest);
            ResponseEntity<String> response = restTemplate.exchange(
                    BACKEND_URL + "/updatepassword", HttpMethod.PUT, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("successMessage", "Password Updated Successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update password. Please try again.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while updating the password. Please try again later.");
        }
        return "redirect:/customer/dashboard";
    }
}