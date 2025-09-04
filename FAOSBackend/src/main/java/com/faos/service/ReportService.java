package com.faos.service;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.dto.CustomerBookingReport;
import com.faos.model.Customer;
import com.faos.repository.BookingRepository;

@Service
public class ReportService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Retrieves a list of customers who have done the maximum number of bookings
     * within the specified date range.
     *
     * @param startDate the start date of the booking range
     * @param endDate   the end date of the booking range
     * @return a list of CustomerBookingReport containing customer details and booking count
     */
    public List<CustomerBookingReport> getCustomersWithMaxBookings(LocalDate startDate, LocalDate endDate) {
        // Retrieve aggregated booking counts per customer within the given date range.
        List<Object[]> results = bookingRepository.findBookingCountsByCustomer(startDate, endDate);

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        // Determine the maximum booking count from the results.
        long maxCount = 0;
        for (Object[] result : results) {
            Long count = (Long) result[1];
            if (count != null && count > maxCount) {
                maxCount = count;
            }
        }

        // Filter the results to include only those customers with the maximum booking count.
        List<com.faos.dto.CustomerBookingReport> reportList = new ArrayList<>();
        for (Object[] result : results) {
            Customer customer = (Customer) result[0];
            Long count = (Long) result[1];
            if (count != null && count == maxCount) {
                CustomerBookingReport report = new CustomerBookingReport();
                report.setConsumerId(customer.getConsumerId());
                report.setConsumerName(customer.getConsumerName());
                report.setBookingCount(count);
                reportList.add(report);
            }
        }

        return reportList;
    }
    public List<Customer> getCustomersWithNoRecentBookings() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        return bookingRepository.findCustomersWithNoBookingsInLastSixMonths(sixMonthsAgo);
    }
}