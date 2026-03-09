    package com.tripease.flight.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.List;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "flights")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Flight {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long flightId;

        @Column(unique = true)
        private String flightNo;

        private String airline;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime depTime;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime arrTime;

        private String depPlace;
        private String arrPlace;

        @Builder.Default
        @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
        private List<FlightSeat> seats = new ArrayList<>();



    }