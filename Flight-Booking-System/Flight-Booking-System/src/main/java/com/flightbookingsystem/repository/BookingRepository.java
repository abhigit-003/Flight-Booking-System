package com.flightbookingsystem.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbookingsystem.entity.Booking;
import com.flightbookingsystem.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
	List<Booking> findByFlightId(Integer flightId);

	List<Booking> findByBookingDate(LocalDate bookingDate);

	List<Booking> findByBookingStatus(BookingStatus bookingStatus);
}
