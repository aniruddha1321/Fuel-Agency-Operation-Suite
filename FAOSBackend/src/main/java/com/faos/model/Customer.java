package com.faos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; 
import java.time.LocalDateTime;
import java.util.List;

 
@Entity
@Table(name = "customers")
public class Customer {
	@Id
	private String consumerId;

	 @NotBlank(message = "Consumer name cannot be blank")
	private String consumerName;
	private String address;
	
	@NotBlank(message = "Contact number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a 10-digit number")
	private String contactNo;
	
	@Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
	 @NotBlank(message = "Email cannot be blank")
	private String email;
	private boolean isActive;
	private LocalDateTime regDate;
	
	private String connType;
	
	 
	
	public Customer() {
		this.regDate = LocalDateTime.now();
		this.isActive = true;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}
	
	
	  @OneToMany(cascade = CascadeType.ALL, mappedBy="customer")
	  //@JoinColumn(name = "bookingId")
	  
	  @com.fasterxml.jackson.annotation.JsonIgnoreProperties("customer") 
	  private List<Booking> bookingList;
	 
	
	
}