package com.flightbookingsystem.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightbookingsystem.dao.BookingDao;
import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Booking;
import com.flightbookingsystem.entity.BookingStatus;
import com.flightbookingsystem.entity.Flight;
import com.flightbookingsystem.entity.Passenger;
import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.exception.NoRecordFoundException;
import com.flightbookingsystem.repository.BookingRepository;
import com.flightbookingsystem.repository.FlightRepository;
import com.flightbookingsystem.repository.PassengerRepository;

@Service
public class BookingService {
	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private PassengerRepository passengerRepository;
	
	public ResponseEntity<ResponseStructure<Booking>> saveAll(Booking bookings){
		
		if(bookings == null || bookings.getFlight() == null) {
			throw new NullPointerException("Flight is required");
		}
		
		if(bookings.getPayment() == null) {
			throw new NullPointerException("Payment is required");
		}
		
		if(bookings.getPassengers() == null || bookings.getPassengers().isEmpty()) {
			throw new NullPointerException("At least one passenger is required");
		}
		
		Optional<Flight> flightOpt = flightRepository.findById(bookings.getFlight().getId());
		if (flightOpt.isEmpty()) {
			throw new NoRecordFoundException("Invalid Flight ID " +bookings.getFlight().getId());
		}
		Flight flight = flightOpt.get();
		bookings.setFlight(flight);
		
		if(flight.getPrice() == null || flight.getTotalSeats() == null) {
			throw new NullPointerException("Flight details incomplete");
		}
		
		double totalAmount = flight.getPrice()*bookings.getPassengers().size();
		bookings.getPayment().setAmount(totalAmount);
		bookings.getPayment().setBooking(bookings);
		
		for(Passenger p:bookings.getPassengers()) {
			p.setBooking(bookings);
		}
		
		Integer totalSeats=flight.getTotalSeats();
		Integer passengersToBook=bookings.getPassengers().size();
		if (passengersToBook >totalSeats) {
		    throw new NoRecordFoundException("Limited seats are available you're trying to book " + passengersToBook +" seats, but only " + totalSeats + " are available on flight " + flight.getId());
		}
		
		else {
			ResponseStructure<Booking> response=new ResponseStructure<Booking>();
			response.setData(bookingDao.saveAll(bookings));
			response.setMessage("Records Saved");
			response.setStatusCode(HttpStatus.OK.value());
			
			return new ResponseEntity<ResponseStructure<Booking>>(response,HttpStatus.OK);
		}
	}


	public ResponseEntity<ResponseStructure<List<Booking>>> findAll() {
		ResponseStructure<List<Booking>> response=new ResponseStructure<List<Booking>>();
		response.setData(bookingDao.findAll());
		response.setMessage("All Records Fetched");
		response.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<Booking>>>(response,HttpStatus.FOUND);
	}
	
	public ResponseEntity<ResponseStructure<Booking>> findById(int id){
		Optional<Booking> opt=bookingDao.findById(id);
		if(opt.isPresent()) {
			ResponseStructure<Booking> response= new ResponseStructure<Booking>();
			response.setData(opt.get());
			response.setMessage("Record Fetched");
			response.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<Booking>>(response,HttpStatus.FOUND); 
		}
		else
			throw new NoRecordFoundException("Couldn't Find Booking Details with id "+id);
	}
	
	 public ResponseEntity<ResponseStructure<List<Booking>>> findByFlightId(int id){
		 Optional<Flight> flightOpt = flightRepository.findById(id);
		 if (flightOpt.isEmpty()) {
			 throw new NoRecordFoundException("Flight not found with ID: " + id);
		 }
		 
		 List<Booking> bookings = bookingRepository.findByFlightId(id);
		 
		 if (bookings==null||bookings.isEmpty()) {
	            throw new NoRecordFoundException("No bookings found for flight ID: " + id);
	        }
		 
		 ResponseStructure<List<Booking>> response = new ResponseStructure<>();
	        response.setStatusCode(HttpStatus.OK.value());
	        response.setMessage("Bookings fetched successfully for flight ID: " + id);
	        response.setData(bookings);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 
	 public ResponseEntity<ResponseStructure<List<Booking>>> findByBookingDate(LocalDate bookingDate){
		 
		 List<Booking> bookings=bookingDao.findByBookingDate(bookingDate);
		 
		 if(bookings.isEmpty()) {
			 throw new NoRecordFoundException("No bookings on the date "+bookingDate);
		 }
		 ResponseStructure<List<Booking>> response = new ResponseStructure<>();
	        response.setStatusCode(HttpStatus.OK.value());
	        response.setMessage("Bookings fetched successfully on the date: " + bookingDate);
	        response.setData(bookings);

	        return new ResponseEntity<>(response, HttpStatus.OK); 
	 }
	 
	 public ResponseEntity<ResponseStructure<List<Passenger>>> findByPassengerInBooking(int id){
		 Optional<Booking> bookingOpt = bookingRepository.findById(id);
		 if (bookingOpt.isEmpty()) {
			 throw new NoRecordFoundException("Booking not found with ID: " + id);
		 }

		 Booking booking = bookingOpt.get();

		 List<Passenger> passengers = booking.getPassengers();

		 if (passengers == null || passengers.isEmpty()) {
			 throw new NoRecordFoundException("No passengers found for booking ID: " + id);
		 }

		 ResponseStructure<List<Passenger>> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Passengers fetched successfully for booking ID: " + id);
		 response.setData(passengers);

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 public ResponseEntity<ResponseStructure<List<Booking>>> findByStatus(BookingStatus status){
		 List<Booking> bookings = bookingRepository.findByBookingStatus(status);

		 if (bookings == null || bookings.isEmpty()) {
			 throw new NoRecordFoundException("No bookings found with status: " + status);
		 }

		 ResponseStructure<List<Booking>> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Bookings fetched successfully with status: " + status);
		 response.setData(bookings);

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 public ResponseEntity<ResponseStructure<Payment>> getPaymentDetails(int id){
		 Optional<Booking> bookingOpt = bookingRepository.findById(id);
		 if (bookingOpt.isEmpty()) {
			 throw new NoRecordFoundException("Booking not found with ID: " + id);
		 }

		 Booking booking = bookingOpt.get();

		 Payment payment = booking.getPayment();
		 if (payment == null) {
			 throw new NoRecordFoundException("No payment details found for booking ID: " + id);
		 }

		 ResponseStructure<Payment> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Payment details fetched successfully for booking ID: " + id);
		 response.setData(payment);

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 public ResponseEntity<ResponseStructure<Booking>> updateBookingStatus(int id, BookingStatus status){
		 Optional<Booking> bookingOpt = bookingRepository.findById(id);
		 if (bookingOpt.isEmpty()) {
			 throw new NoRecordFoundException("Booking not found with ID: " + id);
		 }

		 Booking booking = bookingOpt.get();

		 booking.setBookingStatus(status);
		 Booking updated = bookingRepository.save(booking);

		 ResponseStructure<Booking> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Booking status updated successfully");
		 response.setData(updated);

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 public ResponseEntity<ResponseStructure<Booking>> deleteBooking(int id){
		 Booking booking = bookingRepository.findById(id)
				 .orElseThrow(() -> new NoRecordFoundException("Booking not found with ID: " + id));

		 bookingRepository.delete(booking);

		 ResponseStructure<Booking> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Booking deleted successfully");
		 response.setData(booking);

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 public ResponseEntity<ResponseStructure<List<Booking>>> getBookingsByPaginationAndSorting(int page, int size,
			 String sortBy, String direction){
		 Sort sort = direction.equalsIgnoreCase("DESC") ?
				 Sort.by(sortBy).descending() :
				 Sort.by(sortBy).ascending();

		 Pageable pageable = PageRequest.of(page, size, sort);
		 Page<Booking> bookingPage = bookingRepository.findAll(pageable);

		 if (bookingPage.isEmpty()) {
			 throw new NoRecordFoundException("No bookings found for the given page parameters");
		 }

		 ResponseStructure<List<Booking>> response = new ResponseStructure<>();
		 response.setStatusCode(HttpStatus.OK.value());
		 response.setMessage("Bookings fetched successfully with pagination and sorting");
		 response.setData(bookingPage.getContent());

		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }
}
