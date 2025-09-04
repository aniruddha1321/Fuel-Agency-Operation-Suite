package com.faos.dto;

import java.time.LocalDate;

public class BookingPageView {
    private LocalDate bookingDate;

    public BookingPageView() {
    }

    public BookingPageView(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}

