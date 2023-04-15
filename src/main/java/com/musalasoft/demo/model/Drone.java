package com.musalasoft.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name = "DRONE_ENTRY")
@NoArgsConstructor
@Data
public class Drone {
    @Id
    private String serialNumber; //(100 characters max);
    private DroneType type; //(Lightweight, Middleweight, Cruiserweight, Heavyweight);
    private Integer weight; // limit (500gr max);

    private Integer battery; //capacity(percentage);
    private State state; //(IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
}
