package com.faos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "Bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	private String timeSlot;
	private String deliveryOption;
	private String paymentOption;
	private LocalDate deliveryDate;
	private LocalDate bookingDate;

	// Many-to-One relationship with Cylinder
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Cylinder cylinder;

	// One-to-One relationship with Bill
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "bill_id", referencedColumnName = "billId")
	private Bill bill;

	// Many-to-One relationship with Customer
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "consumerId", referencedColumnName = "consumerId")
	private Customer customer;

	// PrePersist method
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

	public String getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
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

	public Cylinder getCylinder() {
		return cylinder;
	}

	public void setCylinder(Cylinder cylinder) {
		this.cylinder = cylinder;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
