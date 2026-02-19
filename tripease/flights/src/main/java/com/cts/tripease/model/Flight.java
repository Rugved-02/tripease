package com.cts.tripease.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "FLIGHTS")
@Data
public class Flight {
    @Id
    @GeneratedValue(generator = "flight_id_generator")
    @GenericGenerator(name = "flight_id_generator", strategy = "com.cts.tripease.util.FlightIdGenerator")
    private String flightId;

    @Column(unique = true)
    private String flightNo;
    
    private String airline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime depTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrTime;
    private String depPlace;
    private String arrPlace;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<FlightSeat> seats;
}