package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "flight_plane")
@Data
@NoArgsConstructor
public class FlightPlane {
    @Id
    @Column(name = "flight_plane_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "flight", nullable = false)
    private int flight;
    @Column(name = "plane", nullable = false)
    private int plane;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
