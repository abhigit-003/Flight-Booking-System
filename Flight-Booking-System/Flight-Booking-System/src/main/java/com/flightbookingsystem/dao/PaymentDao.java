package com.flightbookingsystem.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.repository.PaymentRepository;

@Repository
public class PaymentDao {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	public Payment save(Payment payments){
		return paymentRepository.save(payments);
	}
	
	public List<Payment> findAll(){
		return paymentRepository.findAll();
	}
	
	public Optional<Payment> findById(int id){
		return paymentRepository.findById(id);
	}
} 
