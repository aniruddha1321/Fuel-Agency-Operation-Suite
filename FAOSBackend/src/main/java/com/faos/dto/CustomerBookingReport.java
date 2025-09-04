package com.faos.dto;


public class CustomerBookingReport {
    private String consumerId;
    private String consumerName;
    private Long bookingCount;
    

    public CustomerBookingReport() {
    }

    public CustomerBookingReport(String consumerId, String consumerName, Long bookingCount) {
        this.consumerId = consumerId;
        this.consumerName = consumerName;
        this.bookingCount = bookingCount;
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

    public Long getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Long bookingCount) {
        this.bookingCount = bookingCount;
    }
}