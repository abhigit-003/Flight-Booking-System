package com.flightbookingsystem.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Booking;
import com.flightbookingsystem.repository.BookingRepository;

@Repository
public class BookingDao {

	@Autowired
	private BookingRepository bookingRepository;
	
	public Booking saveAll(Booking bookings){
		return bookingRepository.save(bookings);
	}

	public List<Booking> findAll() {
		return bookingRepository.findAll();
	}
	
	public Optional<Booking> findById(int id){
		return bookingRepository.findById(id);
	}

	public List<Booking> findByBookingDate(LocalDate bookingDate) {
		return bookingRepository.findByBookingDate(bookingDate);
	}
}

