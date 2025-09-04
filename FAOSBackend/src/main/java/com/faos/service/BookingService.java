package com.faos.service;

//import java.lang.classfile.instruction.ReturnInstruction;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.exception.CustomExceptions;
import com.faos.model.Booking;
import com.faos.model.Customer;
import com.faos.model.Cylinder;
import com.faos.repository.BookingRepository;

import jakarta.transaction.Transactional;
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private  CylinderService cylinderService;
    @Autowired
    private  BillService billService;

    @Transactional
    public Booking saveBooking(Booking booking, String consumerId, String cylinderid) throws CustomExceptions {
        // Retrieve the customer by consumerId
    	
    	//long currentYear = LocalDate.now().getYear();
        long count = countBookingsInLast12Months(consumerId);
        boolean surchargeApplicable = count >= 6;
        // Pass surcharge flag to BillService
//        Bill bill = billService.createBill(booking.getDeliveryOption(), surchargeApplicable);
//
//        booking.setBill(bill);
        booking.setBookingDate(LocalDate.now());
        Optional<Customer> customerOptional = customerService.getCustomerId(consumerId);
        Optional<Cylinder> cylinderOptional = cylinderService.getCustomerById(cylinderid);
        if (customerOptional.isPresent()) {
            booking.setCustomer(customerOptional.get());  // Set the customer in the booking
            booking.setCylinder(cylinderOptional.get());

            return bookingRepository.save(booking);  // Save the booking in the database
        } else {
            throw new CustomExceptions("Customer not found with consumerId: " + consumerId);
        }
    }



	public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public Booking getLastBooking() {
        return bookingRepository.getLastBooking().orElse(null);
    }

    public Booking getLastBookingForCustomer(String consumerId) {
        return bookingRepository.findLastBookingByConsumerId(consumerId).orElse(null);
    }

    public boolean canCustomerBook(String consumerId) {
        Booking lastBooking = getLastBookingForCustomer(consumerId);
        LocalDate date = LocalDate.now();
        System.out.println("Requested Date: " + date);
        if (lastBooking != null) {
            LocalDate lastBookingDate = lastBooking.getBookingDate();
            System.out.println("Last Booking Date: " + lastBookingDate);
            // Check if the requested booking date is at least 30 days after the last booking date
            return !date.isBefore(lastBookingDate.plusDays(30));
        }
        return true;
    }

    public List<Booking> findAllBookings(String consumerId) {
        return bookingRepository.findByConsumerId(consumerId);
    }

    public List<Booking> findByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    public boolean canCustomerBooks(String consumerId) {
        Integer bookingCount = bookingRepository.findByConsumerIdsix(consumerId);
        if (bookingCount == null || bookingCount <= 6) {
            return true;
        }
        return false;
    }
    
    public long countBookingsInLast12Months(String consumerId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(12);
        return bookingRepository.countByConsumerIdAndBookingDateBetween(consumerId, startDate, endDate);
    }


    public long getPreviousBookingId(String consumerId) {
        long id=bookingRepository.findByConsumerid(consumerId).orElse(0L);
        System.out.println(id);
        return  id;// Return 0 if no previous booking is found
    }
    
    public LocalDate getFirstBookingDateForCustomer(String consumerId) {
        Booking firstBooking = bookingRepository.findFirstByCustomer_ConsumerIdOrderByBookingDateAsc(consumerId);
        if (firstBooking != null) {
            return firstBooking.getBookingDate();
        } else {
            throw new IllegalArgumentException("No bookings found for customer with ID: " + consumerId);
        }
    }

    /**
     * Returns the count of bookings made by the customer between startDate and endDate.
     */
    public int getBookingCountInPeriod(String consumerId, LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository.findByCustomer_ConsumerIdAndBookingDateBetween(consumerId, startDate, endDate);
        return bookings.size();
    }
    
    /**
     * Saves the booking entity.
     */
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public  int subCharge(String consumerId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        return bookingRepository.findByConsumerIds(consumerId, startOfYear, today);
    }
}
