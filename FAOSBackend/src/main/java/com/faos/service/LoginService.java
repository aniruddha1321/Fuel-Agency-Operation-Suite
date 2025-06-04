package com.faos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.model.Login;
import com.faos.repository.LoginRepository;

@Service
public class LoginService {

	@Autowired
	LoginRepository loginRepository;
	
	//Authentication of the customer while he logs in
    public boolean authenticateUser(String userId, String password,String userType) {
	        // Fetch login details by userId
	        Login login = loginRepository.findByUserId(userId);

	        // Validate credentials
	        if (login != null && login.getPassword().equals(password) && login.getUserType().equals(userType)) {
	            return true; // Authentication successful
	        }
	        return false; // Authentication failed
	    }
		
		

	    public boolean updatePassword(String userId, String newPassword) {
	        // Retrieve the customer by ID
	    	 
	         Login login = loginRepository.findByUserId(userId);
	         login.setPassword(newPassword);
	         loginRepository.save(login);
	         return true;
	   }
}
