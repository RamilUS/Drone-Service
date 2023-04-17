package com.musalasoft.demo.model.medication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Medication {
    @Id
    @Column(updatable = false)
    private String code;//(allowed only upper case letters, underscore and numbers);

    @Version
    private Long version;

    @Column(updatable = false)
    private String name; //(allowed only letters, numbers, ‘-‘, ‘_’);

    @Column(updatable = false)
    private Integer weight;

    @Lob
    private byte[] image;//(picture of the medication case).

}
