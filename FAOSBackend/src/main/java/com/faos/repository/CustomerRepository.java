package com.faos.repository;

import com.faos.model.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	boolean existsByEmail(String email);

	boolean existsByContactNo(String contactNo);
	
	default void deactivateCustomer(String consumerId) {
        findById(consumerId).ifPresent(customer -> {
            customer.setActive(false);
            save(customer);
        });
    }
	 
	//Fetch all active customers
	@Query("SELECT c FROM Customer c WHERE c.isActive = true")
    List<Customer> getAllCustomers();
	
	//Fetch isActive status from database
	@Query("SELECT c.isActive FROM Customer c WHERE c.consumerId = :consumerId")
    Boolean findIsActiveByConsumerId(@Param("consumerId") String consumerId);
}