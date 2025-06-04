package com.faos.service;

import com.faos.exception.InvalidEntityException;
import com.faos.model.Booking;
import com.faos.model.Customer;
import com.faos.model.Login;
import com.faos.repository.BookingRepository;
import com.faos.repository.CustomerRepository;
import com.faos.repository.LoginRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private LoginRepository loginRepository;
    
    public Customer registerCustomer(Customer customer) {
    	String generatedPassword = generatePassword();
    	customer.setConsumerId(generateConsumerID());
    	Customer savedCustomer = customerRepository.save(customer);
    	
    	 Login login = new Login();
         login.setUserId(customer.getConsumerId());
         login.setPassword(generatedPassword);
         login.setUserType("CUSTOMER");
         loginRepository.save(login);

         // Send email to customer
         emailService.sendEmailToCustomer(
                 customer.getEmail(),
                 customer.getConsumerId(),
                 generatedPassword
         );
         
    	emailService.sendCustomerDetailsToAdmin(savedCustomer);
        return savedCustomer;
    }
    
    public static String generateConsumerID() {       
         String shortUuid = UUID.randomUUID().toString().substring(0, 7);  
         return "CU"+ Integer.toString(Integer.parseInt(shortUuid, 16));

    }
    
    //Generate password for the customer
     

     public static String generatePassword()  {
           String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
            int PASSWORD_LENGTH = 8;

         
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            return password.toString();
        }
     

    
    //Fetch customer by Id
    public Customer getCustomerById(String consumerId) {
        return customerRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + consumerId));
    }
    
    //Update the customer details
    public Customer updateCustomer(String consumerId, Customer updatedCustomer) {
        return customerRepository.findById(consumerId)
                .map(existingCustomer -> {
                    existingCustomer.setConsumerName(updatedCustomer.getConsumerName());
                    existingCustomer.setAddress(updatedCustomer.getAddress());
                    existingCustomer.setContactNo(updatedCustomer.getContactNo());
                    existingCustomer.setEmail(updatedCustomer.getEmail());
                    //existingCustomer.setConnType(updatedCustomer.getConnType());
                    Customer updateCustomer= customerRepository.save(existingCustomer);
                    emailService.sendUpdatedCustomerDetailsToAdmin(updateCustomer);
                    return updateCustomer;
                })
                .orElseThrow(() -> new InvalidEntityException("Customer not found with ID: " + consumerId));
    }
    // Check email id already exist or not
	public boolean isEmailExists(String email) {
		return customerRepository.existsByEmail(email) ;
	}
    
	
	//Get details of all customers
	public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }
	
	//Check Contact already exists or not
	public boolean isContactExists(String contactNo) {
		return customerRepository.existsByContactNo(contactNo);
	}
	
	//Deactivate the customer if exists 
	public void deactivateCustomer(String consumerId) {
	    customerRepository.findById(consumerId).ifPresentOrElse(customer -> {
	        customer.setActive(false);
	        customerRepository.save(customer);
	    }, () -> {
	        throw new InvalidEntityException("Customer not found with ID: " + consumerId);
	    });
	    
	}
	
	//Check if the customer is active or not
	public boolean getActiveStatus(String consumerId ) {
         Boolean isActive = customerRepository.findIsActiveByConsumerId(consumerId);
        
        // Handle null case if consumerId is not found
        if (isActive == null) {
            throw new InvalidEntityException("Customer with consumerId " + consumerId + " does not exists");
        }
        return isActive;
	}
	
	
}	
	
	
	

 