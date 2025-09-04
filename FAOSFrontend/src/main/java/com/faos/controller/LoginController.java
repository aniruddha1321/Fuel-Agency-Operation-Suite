package com.faos.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.faos.model.Login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final String BACKEND_URL = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;
  
    
   @GetMapping("/login")
   public String showLoginPage() {

      return "redirect:/logout";
   }

    @PostMapping("/login")
    public String processLogin(@RequestParam String userId,
            @RequestParam String password,
            @RequestParam String userType,
            Model model,
            HttpSession session ,RedirectAttributes redirectAttributes) {
        try {
            // Create login object matching backend expectations
            Login loginRequest = new Login();
            loginRequest.setUserId(userId);
            loginRequest.setPassword(password);
            loginRequest.setUserType(userType);

            // Make login request to backend
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BACKEND_URL + "/login",
                    loginRequest,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                // Store user info in session
                session.setAttribute("userId", userId);
                session.setAttribute("userType", userType);
                // Redirect based on user type
                if ("ADMIN".equals(userType)) {
                    redirectAttributes.addFlashAttribute("success",response.getBody());
                    return "redirect:/";  // Redirect admin to customer list
                    
                } else if ("CUSTOMER".equals(userType)) {
                    redirectAttributes.addFlashAttribute("success",response.getBody());
                    return "redirect:/customer/dashboard";  // Redirect customer to dashboard
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
               String errorMessage = "Invalid username or password";
                model.addAttribute("error", errorMessage);
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again later.");
        }
        return "index";
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/");  // Redirect to login after logout
    }

    protected boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("userId") != null
                && session.getAttribute("userType") != null;
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
            // Add success message to Flash Attributes
            redirectAttributes.addFlashAttribute("successMessage",response.getBody());
        } else {
            // Add failure message to Flash Attributes
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update password. Please try again.");
        }
    } catch (Exception e) {
        // Add error message to Flash Attributes
        redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while updating the password. Please try again later.");
    }

    // Redirect to the dashboard
    return "redirect:/customer/dashboard";
}

}
