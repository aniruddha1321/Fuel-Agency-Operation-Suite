package com.faos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Nullable
    private Long price=1000L;
    private Long gst;
    private Long deliveryCharge;
    private Long surcharge;

    private Long totalPrice;

    @JsonIgnore
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    private Booking booking;

    // Getters and Setters
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getGst() {
        return gst;
    }

    public void setGst(Long gst) {
        this.gst = gst;
    }

    public Long getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Long deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

	public Long getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(Long surcharge) {
		this.surcharge = surcharge;
	}
    
}
