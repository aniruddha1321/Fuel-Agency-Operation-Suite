package com.faos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.faos.model.Booking;
import com.faos.model.Customer;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM bookings ORDER BY bookingId DESC LIMIT 1", nativeQuery = true)
    Optional<Booking> getLastBooking();

    @Query(value = "SELECT * FROM Bookings WHERE consumerId = :consumerId ORDER BY bookingDate DESC LIMIT 1", nativeQuery = true)
    Optional<Booking> findLastBookingByConsumerId(@Param("consumerId") String consumerId);

    @Query(value = "SELECT * FROM Bookings WHERE consumerId = :consumerId", nativeQuery = true)
    List<Booking> findByConsumerId(@Param("consumerId") String consumerId);

    @Query(value = "SELECT * FROM Bookings WHERE bookingId = :bookingId limit 1", nativeQuery = true)
    List<Booking> findByBookingId(String bookingId);

    @Query(value = "SELECT COUNT(*) FROM Bookings WHERE consumerId = :consumerId GROUP BY bookingDate ORDER BY bookingDate DESC LIMIT 1", nativeQuery = true)
    Integer findByConsumerIdsix(String consumerId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.consumerId = :consumerId AND b.bookingDate BETWEEN :startDate AND :endDate")
    long countByConsumerIdAndBookingDateBetween(@Param("consumerId") String consumerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT COUNT(*) FROM Bookings WHERE consumerId = :consumerId ", nativeQuery = true)
    Optional<Integer> findBookingByConsumerId(String consumerId);

    @Query(value = "SELECT b.bookingId FROM Bookings b WHERE b.consumerId = :consumerId ORDER BY b.bookingId DESC LIMIT 1", nativeQuery = true)
    Optional<Long> findByConsumerid(@Param("consumerId") String consumerId);

    @Query(value = "UPDATE cylinder SET status = 'AVAILABLE', bookingId = null WHERE bookingId = :bookingIds", nativeQuery = true)
    void updateCylinder(long bookingIds);

    @Query("SELECT b.customer, COUNT(b.bookingId) "
            + "FROM Booking b "
            + "WHERE b.bookingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY b.customer")
    List<Object[]> findBookingCountsByCustomer(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.consumerId NOT IN "
            + "(SELECT DISTINCT b.customer.consumerId FROM Booking b WHERE b.bookingDate >= :sixMonthsAgo)")
    List<Customer> findCustomersWithNoBookingsInLastSixMonths(@Param("sixMonthsAgo") LocalDate sixMonthsAgo);
    
    // Retrieves the earliest booking for a customer (by consumerId)
    Booking findFirstByCustomer_ConsumerIdOrderByBookingDateAsc(String consumerId);

    // Retrieves all bookings for a customer between two dates
    List<Booking> findByCustomer_ConsumerIdAndBookingDateBetween(String consumerId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.consumerId = :consumerId AND b.bookingDate BETWEEN :startDate AND :endDate")
    Integer findByConsumerIds(@Param("consumerId") String consumerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

//    long findByConsumerIds(@Param("consumerId") String consumerId, @Param("string") String string, );

}
