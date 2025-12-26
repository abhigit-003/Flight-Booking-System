package com.flightbookingsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Booking;
import com.flightbookingsystem.entity.BookingStatus;
import com.flightbookingsystem.entity.Passenger;
import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@GetMapping("/")
	public String trigger() {
		return "Booking API is running";
	}
	
	@PostMapping("/all")
	public ResponseEntity<ResponseStructure<Booking>> saveAll(@RequestBody Booking bookings){
		return bookingService.saveAll(bookings);
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Booking>>> getAll(){
		return bookingService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Booking>> findById(@PathVariable int id){
		return bookingService.findById(id);
	}
	
	@GetMapping("/flight/{id}")
	public ResponseEntity<ResponseStructure<List<Booking>>> findByFlightId(@PathVariable int id){
		return bookingService.findByFlightId(id);
	}
	
	@GetMapping("/flights/{bookingDate}")
	public ResponseEntity<ResponseStructure<List<Booking>>> findByDate(@PathVariable LocalDate bookingDate ){
		return bookingService.findByBookingDate(bookingDate);
	}
	
	@GetMapping("/pass/{id}")
	public ResponseEntity<ResponseStructure<List<Passenger>>> findByPassengerInBooking(@PathVariable int id){
		return bookingService.findByPassengerInBooking(id);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<ResponseStructure<List<Booking>>> findByStatus(@PathVariable BookingStatus status) {
		return bookingService.findByStatus(status);
	}

	@GetMapping("/{id}/payment")
	public ResponseEntity<ResponseStructure<Payment>> getPaymentDetails(@PathVariable int id) {
		return bookingService.getPaymentDetails(id);
	}

	@PutMapping("/{id}/status/{status}")
	public ResponseEntity<ResponseStructure<Booking>> updateBookingStatus(@PathVariable int id,
			@PathVariable BookingStatus status) {
		return bookingService.updateBookingStatus(id, status);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Booking>> deleteBooking(@PathVariable int id) {
		return bookingService.deleteBooking(id);
	}

	@GetMapping("/page")
	public ResponseEntity<ResponseStructure<List<Booking>>> getBookingsByPaginationAndSorting(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction) {
		return bookingService.getBookingsByPaginationAndSorting(page, size, sortBy, direction);
	}
}
