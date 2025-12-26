package com.flightbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.entity.PaymentMode;
import com.flightbookingsystem.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	
	List<Payment> findByPaymentStatus(PaymentStatus status);
	
	List<Payment> findByAmountGreaterThan(Double amount);
	
	List<Payment> findByPaymentMode(PaymentMode mode);
}
