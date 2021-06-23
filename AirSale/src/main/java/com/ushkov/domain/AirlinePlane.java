package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "airline_plane")
@Embeddable
@Data
@NoArgsConstructor
public class AirlinePlane {
    @Id
    @Column(name = "airline_plane_id")
    @SequenceGenerator(name = "airlinePlaneSequenceGenerator", sequenceName = "airline_plane_airline_plane_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airlinePlaneSequenceGenerator")
    private  long id;
    @Column(name = "plane", nullable = false)
    @Embedded
    private Plane plane;
    @Column(name = "airline", nullable = false)
    @Embedded
    private Airline airline;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
