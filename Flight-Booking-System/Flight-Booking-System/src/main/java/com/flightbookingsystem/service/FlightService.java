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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.flightbookingsystem.dao.FlightDao;
import com.flightbookingsystem.dto.ResponseStructure;
import com.flightbookingsystem.entity.Flight;
import com.flightbookingsystem.exception.NoRecordFoundException;
import com.flightbookingsystem.repository.FlightRepository;

@Service
public class FlightService {
	
	@Autowired
	private FlightDao flightDao;
	@Autowired
	private FlightRepository flightRepository;
	public ResponseEntity<ResponseStructure<List<Flight>>> saveAll(List<Flight> flights){
		if(flights == null || flights.isEmpty()) {
			throw new NullPointerException("Flights list is required");
		}
		ResponseStructure<List<Flight>> response=new ResponseStructure<List<Flight>>();
		response.setData(flightDao.saveAll(flights));
		response.setMessage("Flight Records Saved");
		response.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Flight>>>(response,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<List<Flight>>> findAll(){
		ResponseStructure<List<Flight>> response=new ResponseStructure<List<Flight>>();
		response.setMessage("Flight Records Saved");
		response.setData(flightDao.findAll());
		response.setStatusCode(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<List<Flight>>>(response,HttpStatus.FOUND);

	}

	public ResponseEntity<ResponseStructure<List<Flight>>> getFlightsByPaginationAndSorting(int page, int size,
			String sortBy, String direction){
		Sort sort = direction.equalsIgnoreCase("DESC") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Flight> flightPage = flightRepository.findAll(pageable);

		if (flightPage.isEmpty()) {
			throw new NoRecordFoundException("No flights found for the given page parameters");
		}

		ResponseStructure<List<Flight>> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Flights fetched successfully with pagination and sorting");
		response.setData(flightPage.getContent());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	public ResponseEntity<ResponseStructure<Flight>> findById(int id){
		ResponseStructure<Flight> response=new ResponseStructure<Flight>();
		Optional<Flight> opt=flightDao.findById(id);
		if(opt.isPresent()) {
			response.setMessage("Flight Record fetched by id "+id);
			response.setStatusCode(HttpStatus.FOUND.value());
			response.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Flight>>(response,HttpStatus.FOUND);
		}
		else {
			throw new NoRecordFoundException("Can't find Flight Record with id "+id);
		}
	}
	
	public ResponseEntity<ResponseStructure<List<Flight>>> findBySourceAndDestination(String source, String destination){
		List<Flight> flightList= flightDao.findBySourceAndDestination(source, destination);
		if(!flightList.isEmpty()) {
			ResponseStructure<List<Flight>> response= new ResponseStructure<List<Flight>>();
			response.setMessage("Found Flights between "+source+" and "+destination);
			response.setStatusCode(HttpStatus.FOUND.value());
			response.setData(flightList);
			return new ResponseEntity<ResponseStructure<List<Flight>>>(response,HttpStatus.FOUND);
		}
		else {
			throw new NoRecordFoundException("No Found Flights between "+source+" and "+destination);
		}
	}
	
	public ResponseEntity<ResponseStructure<List<Flight>>> findByAirline(String airline){
		List<Flight> flightList=flightDao.findByAirline(airline);
		if(!flightList.isEmpty()) {
			ResponseStructure<List<Flight>> response=new ResponseStructure<List<Flight>>();
			response.setMessage("Flights found for the airline named "+airline);
			response.setData(flightList);
			response.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<List<Flight>>>(response,HttpStatus.FOUND);
		}
		else {
			throw new NoRecordFoundException("No Found Flights for the airlines named "+airline);
		}
		
	}
	
	public ResponseEntity<ResponseStructure<Flight>> updateFlight(int id, Flight flight){
		if(flight == null) {
			throw new NullPointerException("Flight data is required");
		}
		
		Optional<Flight> opt=flightDao.findById(id);
		if(opt.isPresent()) {
			ResponseStructure<Flight> response=new ResponseStructure<Flight>();
			flight.setId(id);
			response.setMessage("Flight Record Updated");
			response.setStatusCode(HttpStatus.OK.value());
			response.setData(flightDao.updateFlight(flight));
			return new ResponseEntity<ResponseStructure<Flight>>(response,HttpStatus.OK);
		}
		else {
			throw new NoRecordFoundException("Can't find Flight Record by id "+id+" to update");
		}
	}
	
	public ResponseEntity<ResponseStructure<Flight>> deleteFlight(int id){
		Optional<Flight> opt=flightDao.findById(id);
		if(opt.isPresent()) {
			ResponseStructure<Flight> response=new ResponseStructure<Flight>();
			flightDao.deleteFlight(opt.get());
			response.setMessage("Flight Record Deleted");
			response.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Flight>>(response,HttpStatus.OK);
		}
		else {
			throw new NoRecordFoundException("Can't find Flight Record by id "+id+" to delete");
		}
	}
	
}
