package com.faos.model;

import java.time.LocalDate;

 

import jakarta.persistence.*;

@Entity
@Table(name = "Bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
    private String cylinderType;
    private String timeSlot;
	private String deliveryOption;
	private String paymentOption;
	private LocalDate deliveryDate;
	private LocalDate bookingDate = LocalDate.now();

    @PrePersist
	public void prePersist() {
		if (deliveryDate == null) {
			if ("Normal".equals(deliveryOption)) {
				deliveryDate = LocalDate.now().plusDays(3);
			} else if ("Express".equals(deliveryOption)) {
				deliveryDate = LocalDate.now().plusDays(1);
			} else {
				throw new IllegalArgumentException("Invalid delivery option: " + deliveryOption);
			}
		}
	}

	// Getters and Setters
	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getCylinderType() {
		return cylinderType;
	}

	public void setCylinderType(String cylinderType) {
		this.cylinderType = cylinderType;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}
	
	@ManyToOne
	@JoinColumn(name = "consumerId")
	@com.fasterxml.jackson.annotation.JsonIgnoreProperties("bookingList")
	private Customer customerObj;

	public Customer getCustomerObj() {
        return customerObj;
    }

	public void setCustomerObj(Customer savedCustomer) {
		this.customerObj = customerObj;
	}

}
