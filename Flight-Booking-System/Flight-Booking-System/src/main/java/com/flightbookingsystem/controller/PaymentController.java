package com.flightbookingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.entity.PaymentMode;
import com.flightbookingsystem.entity.PaymentStatus;
import com.flightbookingsystem.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	@GetMapping("/")
	public String trigger() {
		return "Payment API is running";
	}
	
	@PostMapping("/all")
	public ResponseEntity<ResponseStructure<Payment>> saveAll(@RequestBody Payment payments ){
		return paymentService.save(payments);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Payment>>> getAll(){
		return paymentService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Payment>> findById(@PathVariable int id){
		return paymentService.findById(id);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<ResponseStructure<List<Payment>>> findByStatus(@PathVariable PaymentStatus status){
		return paymentService.findByStatus(status);
	}

	@GetMapping("/amount/{amount}")
	public ResponseEntity<ResponseStructure<List<Payment>>> findByAmountGreaterThan(@PathVariable double amount){
		return paymentService.findByAmountGreaterThan(amount);
	}

	@GetMapping("/mode/{mode}")
	public ResponseEntity<ResponseStructure<List<Payment>>> findByMode(@PathVariable PaymentMode mode){
		return paymentService.findByPaymentMode(mode);
	}

	@PutMapping("/{id}/status/{status}")
	public ResponseEntity<ResponseStructure<Payment>> updateStatus(@PathVariable int id,
			@PathVariable PaymentStatus status){
		return paymentService.updateStatus(id, status);
	}

	@GetMapping("/page")
	public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentsByPaginationAndSorting(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction){
		return paymentService.getPaymentsByPaginationAndSorting(page, size, sortBy, direction);
	}
}
