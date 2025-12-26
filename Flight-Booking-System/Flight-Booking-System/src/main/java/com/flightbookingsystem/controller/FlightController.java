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
import com.flightbookingsystem.entity.Flight;
import com.flightbookingsystem.service.FlightService;

@RestController
@RequestMapping("/flight")
public class FlightController {
	
	@Autowired
	private FlightService flightService;
	@GetMapping("/")
	public String trigger() {
		return "Flight API is running";
	}
	
	@PostMapping("/flights")
	public ResponseEntity<ResponseStructure<List<Flight>>> saveAll(@RequestBody List<Flight> flights){
		return flightService.saveAll(flights);
	}
	
	@GetMapping("/flights")
	public ResponseEntity<ResponseStructure<List<Flight>>> findAll(){
		return flightService.findAll();
	}

	@GetMapping("/flights/page")
	public ResponseEntity<ResponseStructure<List<Flight>>> getFlightsByPaginationAndSorting(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction) {
		return flightService.getFlightsByPaginationAndSorting(page, size, sortBy, direction);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Flight>> findById(@PathVariable int id){
		return flightService.findById(id);
	}
	
	@GetMapping("/flights/{source}/{destination}")
	public ResponseEntity<ResponseStructure<List<Flight>>> findBySourceAndDestination(@PathVariable String source, @PathVariable  String destination){
		return flightService.findBySourceAndDestination(source, destination);
	}
	
	@GetMapping("/flights/{airline}")
	public ResponseEntity<ResponseStructure<List<Flight>>> findByAirline(@PathVariable String airline){
		return flightService.findByAirline(airline);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Flight>> updateFlight(@PathVariable int id, @RequestBody Flight flight){
		return flightService.updateFlight(id, flight);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Flight>> deleteFlight(@PathVariable int id){
		return flightService.deleteFlight(id);
	}
	 
}
