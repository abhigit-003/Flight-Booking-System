package com.flightbookingsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flightbookingsystem.dao.PassengerDao;
import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Booking;
import com.flightbookingsystem.entity.Passenger;
import com.flightbookingsystem.exception.NoRecordFoundException;
import com.flightbookingsystem.repository.BookingRepository;
import com.flightbookingsystem.repository.PassengerRepository;

@Service
public class PassengerService {
	
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private PassengerDao passengerDao;
	@Autowired
	private PassengerRepository passengerRepository;
	
	public ResponseEntity<ResponseStructure<Passenger>> addPassenger(Passenger passenger){
		if(passenger == null) {
			throw new NullPointerException("Passenger data is required");
		}
		ResponseStructure<Passenger> response=new ResponseStructure<Passenger>();
		response.setData(passengerDao.save(passenger));
		response.setMessage("Passenger added successfully");
		response.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<Passenger>>(response,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<List<Passenger>>> saveAll(List<Passenger> passengers){
		if(passengers == null || passengers.isEmpty()) {
			throw new NullPointerException("Passengers list is required");
		}
		ResponseStructure<List<Passenger>> response=new ResponseStructure<List<Passenger>>();
		response.setData(passengerDao.saveAll(passengers));
		response.setMessage("Record saved");
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Passenger>>>(response,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Passenger>>> findAll(){
		ResponseStructure<List<Passenger>> response=new ResponseStructure<List<Passenger>>();
		response.setData(passengerDao.findAll());
		response.setMessage("All passenger records fetched");
		response.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<Passenger>>>(response,HttpStatus.FOUND);
	}

	public ResponseEntity<ResponseStructure<Passenger>> findById(int id){
		Optional<Passenger> opt = passengerDao.findById(id);
		if(opt.isPresent()) {
			ResponseStructure<Passenger> response=new ResponseStructure<Passenger>();
			response.setData(opt.get());
			response.setMessage("Passenger record fetched");
			response.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<Passenger>>(response,HttpStatus.FOUND);
		}else {
			throw new NoRecordFoundException("Couldn't find passenger with id "+id);
		}
	}
	
	public ResponseEntity<ResponseStructure<Passenger>> updatePassenger(int id, Passenger incomingPassenger) {
		if(incomingPassenger == null) {
			throw new NullPointerException("Passenger data is required");
		}
		
		Optional<Passenger> opt = passengerDao.findById(id);
		if (!opt.isPresent()) {
			throw new NoRecordFoundException("Passenger not found with id " + id);
		}
		
		Passenger existingPassenger = opt.get();
		
		if (incomingPassenger.getName() != null) {
			existingPassenger.setName(incomingPassenger.getName());
		}
		if (incomingPassenger.getAge() != null) {
			existingPassenger.setAge(incomingPassenger.getAge());
		}
		if (incomingPassenger.getGender() != null) {
			existingPassenger.setGender(incomingPassenger.getGender());
		}
		if (incomingPassenger.getSeatNumber() != null) {
			existingPassenger.setSeatNumber(incomingPassenger.getSeatNumber());
		}
		
		if (incomingPassenger.getBooking() != null && incomingPassenger.getBooking().getId() != null) {
			final Integer bookingId = incomingPassenger.getBooking().getId();
			Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
			if (bookingOpt.isEmpty()) {
				throw new NoRecordFoundException("Booking not found with id " + bookingId);
			}
			existingPassenger.setBooking(bookingOpt.get());
		}
		
		Passenger savedPassenger = passengerRepository.save(existingPassenger);
		
		ResponseStructure<Passenger> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Passenger updated successfully");
		response.setData(savedPassenger);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<Passenger>> deleteById(int id){
		Optional<Passenger> opt=passengerDao.findById(id);
		if(opt.isEmpty()) {
			throw new NoRecordFoundException("Passenger not found with id " + id);
		}
		else {
			Passenger passenger=opt.get();
			passengerDao.delete(passenger);
			ResponseStructure<Passenger> response = new ResponseStructure<>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Passenger deleted successfully");
			return new ResponseEntity<ResponseStructure<Passenger>>(response,HttpStatus.OK);
		}
		
	}
 
	public ResponseEntity<ResponseStructure<List<Passenger>>> getPassengersByPaginationAndSorting(int page, int size,
			String sortBy, String direction){
		Sort sort = direction.equalsIgnoreCase("DESC") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Passenger> passengerPage = passengerRepository.findAll(pageable);

		if(passengerPage.isEmpty()) {
			throw new NoRecordFoundException("No passengers found for the given page parameters");
		}

		ResponseStructure<List<Passenger>> response=new ResponseStructure<List<Passenger>>();
		response.setData(passengerPage.getContent());
		response.setMessage("Passengers fetched successfully with pagination and sorting");
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Passenger>>>(response,HttpStatus.OK);
	}
}
