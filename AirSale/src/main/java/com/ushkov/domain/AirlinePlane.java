package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "airline_plane")
@Data
@NoArgsConstructor
public class AirlinePlane {
    @Id
    @Column(name = "airline_plane_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  long id;
    @Column(name = "plane", nullable = false)
    private long plane;
    @Column(name = "airline", nullable = false)
    private short airline;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
