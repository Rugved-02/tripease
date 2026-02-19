package com.cts.flightEntityTripease.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.flightEntityTripease.model.Flight;
import com.cts.flightEntityTripease.service.FlightService;

@RestController
@RequestMapping("flight")
@CrossOrigin(origins = "http://localhost:8080")
public class FlightController {
	
	private FlightService flightService;
	
	public FlightController(@Qualifier("dbServiceImpl") FlightService flightService) {
		
		this.flightService = flightService;
	}

	

	
	@GetMapping("")
	ResponseEntity<List<Flight>> getFlights() {
		
		ResponseEntity<List<Flight>> responseResult = new ResponseEntity<List<Flight>>(flightService.getAllFlights(),HttpStatus.OK);
		
		return responseResult;
	}
	
	
	@GetMapping("id/{id}")
	ResponseEntity<Flight> getFlightById(@PathVariable int id) {
		
		HttpStatus httpCode;
		Flight resFlight  = flightService.getFlightById(id);
		if( resFlight != null) {
			httpCode = HttpStatus.FOUND;
		}else {
			httpCode = HttpStatus.NOT_FOUND;
		}
		
		ResponseEntity<Flight> responseResult = new ResponseEntity<Flight>(resFlight,httpCode);
		
		return responseResult;
	}
	
	
	@PostMapping()
	Flight addFlight(@RequestBody Flight flight) {
		return flightService.addFlight(flight);
	}
}
