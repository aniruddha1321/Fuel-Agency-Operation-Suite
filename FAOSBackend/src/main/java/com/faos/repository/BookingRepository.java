package com.faos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faos.model.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long> {

}
