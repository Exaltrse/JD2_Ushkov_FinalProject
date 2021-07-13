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
@Table(name = "flight")
@Data
@NoArgsConstructor
public class Flight {
    @Id
    @Column(name = "flight_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "flight_number", length = 7, nullable = false)
    private String number;
    @Column(name = "airline", nullable = false)
    private short airline;
    @Column(name = "airport_departure", nullable = false)
    private short departure;
    @Column(name = "airport_destination", nullable = false)
    private short destination;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
