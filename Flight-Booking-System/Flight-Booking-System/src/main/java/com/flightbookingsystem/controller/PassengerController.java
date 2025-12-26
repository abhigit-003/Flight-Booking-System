package com.flightbookingsystem.controller;

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
import com.flightbookingsystem.entity.Passenger;
import com.flightbookingsystem.service.PassengerService;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
	
	@Autowired
	private PassengerService passengerService;
	@GetMapping("/")
	public String trigger() {
		return "Passenger API is running";
	}
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Passenger>> addPassenger(@RequestBody Passenger passenger){
		return passengerService.addPassenger(passenger);
	}
	
	@PostMapping("/all")
	public ResponseEntity<ResponseStructure<List<Passenger>>> saveAll(@RequestBody List<Passenger> passengers){
		return passengerService.saveAll(passengers);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Passenger>>> getAll(){
		return passengerService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Passenger>> findById(@PathVariable int id){
		return passengerService.findById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Passenger>> updatePassengerById(@PathVariable int id,@RequestBody Passenger passenger){
		return passengerService.updatePassenger(id,passenger);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Passenger>> deletePassengerById(@PathVariable int id){
		return passengerService.deleteById(id);
	}

	@GetMapping("/page")
	public ResponseEntity<ResponseStructure<List<Passenger>>> getPassengersByPaginationAndSorting(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction) {
		return passengerService.getPassengersByPaginationAndSorting(page, size, sortBy, direction);
	}
}
