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
@Table(name = "flight_plane")
@Embeddable
@Data
@NoArgsConstructor
public class FlightPlane {
    @Id
    @Column(name = "flight_plane_id")
    @SequenceGenerator(name = "flightPlaneSequenceGenerator", sequenceName = "flight_plane_flight_plane_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flightPlaneSequenceGenerator")
    private long id;
    @Column(name = "flight", nullable = false)
    @Embedded
    private Flight flight;
    @Column(name = "plane", nullable = false)
    private Plane plane;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
