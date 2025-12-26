package com.flightbookingsystem.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Entity
@Data
public class Booking {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@CreationTimestamp
	private LocalDate bookingDate;
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;
	
	
	@ManyToOne
	@JoinColumn(name="flightId")//foreign key is in this Table by name "flightId"
	private Flight flight;
	
	@OneToOne(mappedBy = "booking",cascade=CascadeType.ALL)
	private Payment payment;
	
	@OneToMany(mappedBy = "booking",cascade=CascadeType.ALL)
	private List<Passenger> passengers;
}
