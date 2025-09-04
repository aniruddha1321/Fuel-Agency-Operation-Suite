package com.faos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.faos.model.Customer;

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
	
    @Query(value = "SELECT * FROM customers WHERE consumerId = :consumerId limit 1", nativeQuery = true)
    List<Customer> findByConsumerId(String consumerId);

    // @Query(value = "SELECT * FROM customers WHERE consumerId = :consumerId limit 1", nativeQuery = true)
    // Optional<Customer> findById(String consumerId);
	
}