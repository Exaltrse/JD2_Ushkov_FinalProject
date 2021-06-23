package com.ushkov.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "plane")
@Embeddable
@Data
@NoArgsConstructor
public class Plane {
    @Id
    @Column(name = "plane_id")
    @SequenceGenerator(name = "planeSequenceGenerator", sequenceName = "Plane_plane_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planeSequenceGenerator")
    private int id;
    @Column(name = "aircraft_number", length = 9, nullable = false, unique = true)
    private String aircraftNumber;
    @Column(name = "properties", length = 100)
    private String properties;

}
