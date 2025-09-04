package com.faos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.faos.model.Customer;
import com.faos.repository.CylinderRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private CylinderRepository cylinderRepository;

    @Value("${app.admin.email}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    SimpleMailMessage message = new SimpleMailMessage();

    public void sendCustomerDetailsToAdmin( Customer customer) {
        // Compose the email
        message.setTo(adminEmail);
        message.setSubject("New Customer Registered");
        message.setText("A new customer has been registered:\n\n" +buildCustomerDetailsMessage(customer));

        // Send the email
        mailSender.send(message);
    }

    private String buildCustomerDetailsMessage(Customer customer) {
        return "Consumer ID: " + customer.getConsumerId() + "\n" +
               "Name: " + customer.getConsumerName() + "\n" +
               "Address: " + customer.getAddress() + "\n" +
               "Contact No: " + customer.getContactNo() + "\n" +
               "Email: " + customer.getEmail() + "\n" +
               "Connection Type: " + customer.getConnType() + "\n" +
               "Registration Date: " + customer.getRegDate() ;
    }
    
    //Send email when customer details are updated
    public void sendUpdatedCustomerDetailsToAdmin( Customer customer) {
        // Compose the email
        message.setTo(adminEmail);
        message.setSubject("Customer datails Updated");
        message.setText("Customer details has been updated:\n\n" +buildCustomerDetailsMessage(customer));

        // Send the email
        mailSender.send(message);
    }
    
    //Send mail to admin when customer deactivates
    public void sendMailWhenUserDeactivates(String consumerId) {
    	 message.setTo(adminEmail);
         message.setSubject("Customer is deactivated");
         message.setText("Customer with this id"+consumerId+"has been deactivated");
         mailSender.send(message);
    }
    //Send email to the customer
    public void sendEmailToCustomer(String toEmail, String consumerId, String password) {
         
        message.setTo(toEmail);
        message.setSubject("Welcome to Our Service");
        message.setText("Dear Customer,\n\n" +
                        "Your registration is successful.\n" +
                        "Consumer ID: " + consumerId + "\n" +
                        "Password: " + password + "\n\n" +
                        "Thank you,\nFuel Agency");
        mailSender.send(message);
    }
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
    public void sendLowStockAlert(int count) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("acharykeerti@gmail.com");
        message.setSubject("Low Cylinder Stock Alert");
        message.setText("Warning: The total number of cylinders is below 10. Current count: " + count);

        mailSender.send(message);
        System.out.println("Low stock alert email sent to admin.");
    }
    
    @Scheduled(fixedRate = 86400000) // Runs every 24 hours  10000
    public void checkCylinderStock() {
        int totalCylinders = (int) cylinderRepository.count();
        System.out.println("Mail sent!!!!!!!!!!!!!!!");
        if (totalCylinders < 10) {
            sendLowStockAlert(totalCylinders);
        }
    }
    
}






