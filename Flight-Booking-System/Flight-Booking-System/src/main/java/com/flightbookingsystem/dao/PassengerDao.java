package com.flightbookingsystem.dao;

import java.util.List;
import java.util.Optional;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flightbookingsystem.entity.Passenger;
import com.flightbookingsystem.repository.PassengerRepository;

@Repository
public class PassengerDao {
	
	@Autowired
	private PassengerRepository passengerRepository;
	
	public List<Passenger> saveAll(List<Passenger> passengers){
		return passengerRepository.saveAll(passengers);
	}

	public List<Passenger> findAll(){
		return passengerRepository.findAll();
	}

	public Optional<Passenger> findById(int id){
		return passengerRepository.findById(id);
	}
	
	public Passenger save(Passenger passenger) {
		return passengerRepository.save(passenger);
	}
	
	public void delete(Passenger passenger) {
		 passengerRepository.delete(passenger);
	}
}
