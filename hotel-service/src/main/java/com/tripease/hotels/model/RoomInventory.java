package com.tripease.hotels.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "room_inventory", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"hotelId", "inventoryDate"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hotelId;

    private LocalDate inventoryDate;

    private int availableRooms;

    private double priceOnDate;
    
}