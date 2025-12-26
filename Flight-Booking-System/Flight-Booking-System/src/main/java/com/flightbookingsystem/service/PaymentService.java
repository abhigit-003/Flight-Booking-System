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

import com.flightbookingsystem.dao.PaymentDao;
import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Payment;
import com.flightbookingsystem.entity.PaymentMode;
import com.flightbookingsystem.entity.PaymentStatus;
import com.flightbookingsystem.exception.NoRecordFoundException;
import com.flightbookingsystem.repository.PaymentRepository;

@Service
public class PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentRepository paymentRepository;

	public ResponseEntity<ResponseStructure<Payment>> save(Payment payment) {
		if(payment == null) {
			throw new NullPointerException("Payment data is required");
		}
        ResponseStructure<Payment> response = new ResponseStructure<>();
        response.setData(paymentDao.save(payment));
        response.setMessage("Payment Processed Successfully");
        response.setStatusCode(HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	public ResponseEntity<ResponseStructure<List<Payment>>> findAll(){
		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(paymentDao.findAll());
		response.setMessage("All payment records fetched");
		response.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.FOUND);
	}

	public ResponseEntity<ResponseStructure<Payment>> findById(int id){
		Optional<Payment> opt = paymentDao.findById(id);
		if(opt.isPresent()) {
			ResponseStructure<Payment> response = new ResponseStructure<>();
			response.setData(opt.get());
			response.setMessage("Payment record fetched");
			response.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<Payment>>(response, HttpStatus.FOUND);
		}else {
			throw new NoRecordFoundException("Couldn't find payment with id "+id);
		}
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> findByStatus(PaymentStatus status){
		List<Payment> payments = paymentRepository.findByPaymentStatus(status);
		if(payments == null || payments.isEmpty()) {
			throw new NoRecordFoundException("No payments found with status: "+status);
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(payments);
		response.setMessage("Payments fetched successfully with status: "+status);
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> findByAmountGreaterThan(double amount){
		List<Payment> payments = paymentRepository.findByAmountGreaterThan(amount);
		if(payments == null || payments.isEmpty()) {
			throw new NoRecordFoundException("No payments found with amount greater than: "+amount);
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(payments);
		response.setMessage("Payments fetched successfully with amount greater than: "+amount);
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> findByPaymentMode(PaymentMode mode){
		List<Payment> payments = paymentRepository.findByPaymentMode(mode);
		if(payments == null || payments.isEmpty()) {
			throw new NoRecordFoundException("No payments found with mode: "+mode);
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(payments);
		response.setMessage("Payments fetched successfully with mode: "+mode);
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<Payment>> updateStatus(int id, PaymentStatus status){
		Optional<Payment> paymentOpt = paymentRepository.findById(id);
		if (paymentOpt.isEmpty()) {
			throw new NoRecordFoundException("Payment not found with ID: " + id);
		}

		Payment payment = paymentOpt.get();

		payment.setPaymentStatus(status);
		Payment updated = paymentRepository.save(payment);

		ResponseStructure<Payment> response = new ResponseStructure<>();
		response.setData(updated);
		response.setMessage("Payment status updated successfully");
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<Payment>>(response, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Payment>>> getPaymentsByPaginationAndSorting(int page, int size,
			String sortBy, String direction){
		Sort sort = direction.equalsIgnoreCase("DESC") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Payment> paymentPage = paymentRepository.findAll(pageable);

		if(paymentPage.isEmpty()) {
			throw new NoRecordFoundException("No payments found for the given page parameters");
		}

		ResponseStructure<List<Payment>> response = new ResponseStructure<>();
		response.setData(paymentPage.getContent());
		response.setMessage("Payments fetched successfully with pagination and sorting");
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Payment>>>(response, HttpStatus.OK);
	}
}
