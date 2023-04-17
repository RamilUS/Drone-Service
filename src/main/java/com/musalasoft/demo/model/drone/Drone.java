package com.musalasoft.demo.model.drone;

import com.musalasoft.demo.model.medication.Medication;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor()
@Data
public class Drone {
    @Id
    @Size(min = 1, max = 100)
    @Column(updatable = false)
    private String serialNumber;

    @Version
    private Long version;

    @Column(updatable = false)
    private DroneType type; //(Lightweight, Middleweight, Cruiserweight, Heavyweight);

    @Column(nullable = false)
    @Min(0)
    @Max(500)
    private Integer weight;

    @Min(0)
    @Max(100)
    private Integer batteryLevel;

    @Enumerated(EnumType.STRING)
    private State state; //(IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "LOADED_MEDS",
            joinColumns = {@JoinColumn(name = "MASTER_ID", referencedColumnName = "SERIAL_NUMBER")}
    )
    @MapKeyColumn(name = "MED_CODE")
    @Column(name = "QUANTITY")
    private Map<String, Integer> loadedMeds;

}
