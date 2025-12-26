package com.flightbookingsystem.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flightbookingsystem.entity.Flight;
import com.flightbookingsystem.repository.FlightRepository;

@Repository
public class FlightDao {
	@Autowired
	private FlightRepository flightRepository;
	
	public List<Flight> saveAll(List<Flight> flights){
		return flightRepository.saveAll(flights);
	}
	
	public List<Flight> findAll(){
		return flightRepository.findAll();
	}
	
	public Optional<Flight> findById(int id) {
		return flightRepository.findById(id);
	}
	
	public List<Flight> findBySourceAndDestination(String source,String destination){
		return flightRepository.findBySourceAndDestination(source, destination);
	}
	
	public List<Flight> findByAirline(String airline){
		return flightRepository.findByAirline(airline);
	}
	
	public Flight updateFlight(Flight flight) {
		return flightRepository.save(flight);
	}
	
	public void deleteFlight(Flight flight){
		flightRepository.delete(flight);
	}
}
