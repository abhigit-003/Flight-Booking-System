package com.flightbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbookingsystem.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

}
