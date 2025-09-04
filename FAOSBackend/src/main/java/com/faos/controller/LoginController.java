package com.faos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.faos.model.Login;
import com.faos.service.CustomerService;
import com.faos.service.LoginService;

@RestController
public class LoginController {

	
	@Autowired
	LoginService loginService;
	
	@Autowired
	CustomerService customerService;
	
	        //To login the customer
            @PostMapping("/login")
		    public ResponseEntity<String> login(@RequestBody Login  login ) {
            	boolean isAuthenticated=loginService.authenticateUser(login.getUserId(), login.getPassword(),login.getUserType());
            	if(isAuthenticated) {
		        	if(login.getUserType().equals("ADMIN")) {
		        		 return ResponseEntity.ok("Login successful!");
		        	}
		        	else {
		        	       boolean isActive = customerService.getActiveStatus(login.getUserId() );
		        	       if (isActive) {
			                   return ResponseEntity.ok("Login successful!");
			               }
		        	       else {
			                  return ResponseEntity.status(401).body("Customer with this id is no longer active");
			               }
		            }
            	}
		        else 
		        	return ResponseEntity.status(401).body("Invalid UserId  or password");
		        
		       }
		 
	        //Update password of the customer
            @PutMapping("/updatepassword")
		    public ResponseEntity<String> updatePassword(@RequestBody Login login){
            	
			    boolean isUpdated= loginService.updatePassword(login.getUserId(), login.getPassword());
			    if(isUpdated) {
				    return ResponseEntity.ok("Password updated successfully!");
			    }
			    return ResponseEntity.status(401).body("Password not updated");
		 }
}
