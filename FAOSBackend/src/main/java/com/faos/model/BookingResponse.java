package com.faos.model;

import com.faos.model.Booking;

public class BookingResponse {
	    private Booking booking;
	    private String message;
	    private boolean surchargeApplied;

	    public BookingResponse(Booking booking, String message, boolean surchargeApplied) {
	        this.booking = booking;
	        this.message = message;
	        this.surchargeApplied = surchargeApplied;
	    }

		public Booking getBooking() {
			return booking;
		}

		public void setBooking(Booking booking) {
			this.booking = booking;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isSurchargeApplied() {
			return surchargeApplied;
		}

		public void setSurchargeApplied(boolean surchargeApplied) {
			this.surchargeApplied = surchargeApplied;
		}

	    
	}

